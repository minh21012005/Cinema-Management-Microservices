package com.example.service.impl;

import com.example.domain.entity.AuthUser;
import com.example.domain.entity.EmailOtpVerification;
import com.example.domain.entity.Role;
import com.example.domain.entity.UserProfileDTO;
import com.example.domain.request.CreateUserRequest;
import com.example.domain.request.UserUpdateDTO;
import com.example.domain.request.UserUpdateProfileDTO;
import com.example.domain.request.VerifyOtpRequest;
import com.example.domain.response.ResUserDTO;
import com.example.repository.AuthUserRepository;
import com.example.repository.EmailOtpVerificationRepository;
import com.example.service.AuthUserService;
import com.example.service.RoleService;
import com.example.util.error.IdInvalidException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthUserServiceImpl extends BaseServiceImpl<AuthUser, Long, CreateUserRequest, ResUserDTO> implements AuthUserService {

    @Value("${app.rabbitmq.send-routing-key}")
    private String sendRoutingKey;

    @Value("${app.rabbitmq.send-routing-key-update}")
    private String sendRoutingKeyUpdate;

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    private final AuthUserRepository authUserRepository;
    private final RabbitTemplate rabbitTemplate;
    private final EmailOtpVerificationRepository emailOtpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final RoleService roleService;

    public AuthUserServiceImpl(AuthUserRepository authUserRepository,
                               RabbitTemplate rabbitTemplate,
                               PasswordEncoder passwordEncoder,
                               RoleService roleService,
                               ObjectMapper objectMapper,
                               EmailOtpVerificationRepository emailOtpVerificationRepository) {
        super(authUserRepository);
        this.authUserRepository = authUserRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.objectMapper = objectMapper;
        this.emailOtpVerificationRepository = emailOtpVerificationRepository;
    }

    @Override
    public boolean isEmailExist(String email) {
        return authUserRepository.existsByEmail(email);
    }

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return authUserRepository.findByEmail(email);
    }

    @Override
    public void updateRefreshToken(String refreshToken, String username) {
        AuthUser currentUser = authUserRepository.findByEmail(username).orElse(null);
        if (currentUser != null) {
            currentUser.setRefreshToken(refreshToken);
            this.authUserRepository.save(currentUser);
        }
    }

    @Override
    public Optional<AuthUser> findByRefreshTokenAndEmail(String refreshToken, String email) {
        return this.authUserRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }

    @Override
    public void updateUserToken(String token, String email) {
        AuthUser currentUser = this.findByEmail(email).orElse(null);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.authUserRepository.save(currentUser);
        }
    }

    @Override
    public ResUserDTO updateUser(AuthUser user, Role role, UserUpdateDTO dto) {
        String oldEmail = user.getEmail();

        user.setEmail(dto.getEmail());
        user.setRole(role);
        user.setEnabled(dto.isEnabled());

        AuthUser saved = authUserRepository.save(user);

        UserUpdateProfileDTO profileDTO = new UserUpdateProfileDTO();
        profileDTO.setName(dto.getName());
        profileDTO.setEmail(dto.getEmail());
        profileDTO.setPhone(dto.getPhone());
        profileDTO.setDateOfBirth(dto.getDateOfBirth());
        profileDTO.setGender(dto.getGender());
        profileDTO.setAddress(dto.getAddress());
        profileDTO.setRoleId(role.getId());
        profileDTO.setOldEmail(oldEmail);
        profileDTO.setAuthId(user.getId());

        this.rabbitTemplate.convertAndSend(
                exchangeName, sendRoutingKeyUpdate, profileDTO
        );

        ResUserDTO res = new ResUserDTO();
        res.setId(saved.getId());
        res.setName(dto.getName());
        res.setEmail(dto.getEmail());
        res.setRole(role.getCode());

        return res;
    }

    @Override
    public ResUserDTO updateUserStatus(AuthUser user) {
        user.setEnabled(!user.isEnabled());
        AuthUser saved = authUserRepository.save(user);

        ResUserDTO res = new ResUserDTO();
        res.setId(saved.getId());
        res.setEmail(saved.getEmail());
        res.setRole(saved.getRole().getCode());
        res.setEnabled(saved.isEnabled());

        return res;
    }

    @Override
    public ResUserDTO registerVerify(VerifyOtpRequest req) throws IdInvalidException, JsonProcessingException {
        // 1️⃣ Tìm OTP còn hiệu lực và chưa verify
        EmailOtpVerification otpRecord = emailOtpVerificationRepository
                .findValidToken(req.getEmail(), req.getOtp(), LocalDateTime.now())
                .orElseThrow(() -> new IdInvalidException("Mã OTP không hợp lệ hoặc đã hết hạn"));

        // 2️⃣ Parse lại dữ liệu gốc
        CreateUserRequest userData = objectMapper.readValue(
                otpRecord.getRawData(),
                CreateUserRequest.class
        );

        // 3️⃣ Tạo AuthUser thật
        String hashedPassword = passwordEncoder.encode(userData.getPassword());
        Role role = roleService.findByCode("CUSTOMER").orElseThrow();

        AuthUser authUser = new AuthUser();
        authUser.setEmail(userData.getEmail());
        authUser.setPassword(hashedPassword);
        authUser.setRole(role);

        AuthUser savedUser = this.save(authUser);

        // 4️⃣ Gửi event sang user-service
        UserProfileDTO profileEvent = new UserProfileDTO(
                userData.getName(),
                userData.getPhone(),
                userData.getDateOfBirth(),
                userData.getGender(),
                userData.getEmail(),
                userData.getAddress(),
                role.getId(),
                savedUser.getId()
        );
        rabbitTemplate.convertAndSend(exchangeName, sendRoutingKey, profileEvent);

        // 5️⃣ Đánh dấu OTP đã dùng
        otpRecord.setVerified(true);
        emailOtpVerificationRepository.save(otpRecord);

        ResUserDTO res = new ResUserDTO();
        res.setId(savedUser.getId());
        res.setName(profileEvent.getName());
        res.setEmail(profileEvent.getEmail());
        res.setRole(role.getCode());
        res.setEnabled(savedUser.isEnabled());

        return res;
    }

    @Override
    public boolean existsById(Long aLong) {
        return authUserRepository.existsById(aLong);
    }
}
