package com.example.service.impl;

import com.example.client.AuthClient;
import com.example.domain.entity.User;
import com.example.domain.entity.UserAuthDTO;
import com.example.domain.request.CreateUserRequest;
import com.example.domain.request.UserUpdateDTO;
import com.example.domain.response.ResUserDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.service.specification.UserSpecification;
import com.example.util.constant.GenderEnum;
import com.example.util.error.IdInvalidException;
import feign.FeignException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl
        extends BaseServiceImpl<User, Long, CreateUserRequest, ResUserDTO>
        implements UserService {

    @Value("${app.rabbitmq.send-routing-key}")
    private String sendRoutingKey;

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final AuthClient authClient;

    public UserServiceImpl(UserRepository userRepository, RabbitTemplate rabbitTemplate,
                           AuthClient roleClient, AuthClient authClient) {
        super(userRepository);
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.authClient = roleClient;
    }

    @Override
    public boolean isPhoneExist(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public ResultPaginationDTO fetchAllUser(String emailFilter, Long roleFilter, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(
                UserSpecification.findUsersWithFilters(emailFilter, roleFilter), pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber());
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> {
                    try {
                        return this.convertToResUserDTO(item);
                    } catch (IdInvalidException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    @Override
    public ResUserDTO createUser(CreateUserRequest dto) throws IdInvalidException {
        boolean isEmailExist = this.userRepository.existsByEmail(dto.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email " + dto.getEmail() + " đã tồn tại, vui lòng sử dụng email khác.");
        }

        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new IdInvalidException("Phone " + dto.getPhone() + " đã tồn tại.");
        }

        String codeRole;
        try {
            codeRole = authClient.getRoleCode(dto.getRoleId());
        } catch (FeignException.BadRequest e) {
            throw new IdInvalidException("Role ID không hợp lệ: " + dto.getRoleId());
        }

        UserAuthDTO userAuthDTO = new UserAuthDTO();
        userAuthDTO.setEmail(dto.getEmail());
        userAuthDTO.setPassword(dto.getPassword());
        userAuthDTO.setRoleId(dto.getRoleId());

        // Publish event sang auth-service
        this.rabbitTemplate.convertAndSend(
                exchangeName, sendRoutingKey, userAuthDTO
        );

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setGender(GenderEnum.valueOf(dto.getGender()));
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setRoleId(dto.getRoleId());

        User saved = userRepository.save(user);

        ResUserDTO res = new ResUserDTO();
        res.setId(saved.getId());
        res.setName(saved.getName());
        res.setEmail(saved.getEmail());
        res.setRole(codeRole);

        return res;
    }

    @Override
    public ResUserDTO updateUser(User userDb, UserUpdateDTO dto) throws IdInvalidException {
        userDb.setName(dto.getName());
        userDb.setPhone(dto.getPhone());
        userDb.setGender(GenderEnum.valueOf(dto.getGender()));
        userDb.setDateOfBirth(dto.getDateOfBirth());
        userDb.setAddress(dto.getAddress());

        User saved = userRepository.save(userDb);

        String codeRole;
        try {
            codeRole = authClient.getRoleCode(saved.getRoleId());
        } catch (FeignException.BadRequest e) {
            throw new IdInvalidException("Role ID không hợp lệ: " + saved.getRoleId());
        }

        ResUserDTO res = new ResUserDTO();
        res.setId(saved.getId());
        res.setName(saved.getName());
        res.setEmail(saved.getEmail());
        res.setRole(codeRole);

        return res;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public ResUserDTO convertToResUserDTO(User user) throws IdInvalidException {
        ResUserDTO res = new ResUserDTO();
        boolean isUserEnabled = authClient.isUserEnabled(user.getEmail());

        String codeRole;
        try {
            codeRole = authClient.getRoleCode(user.getRoleId());
        } catch (FeignException.BadRequest e) {
            throw new IdInvalidException("Role ID không hợp lệ: " + user.getRoleId());
        }

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setRole(codeRole);
        res.setEnabled(isUserEnabled);

        return res;
    }
}

