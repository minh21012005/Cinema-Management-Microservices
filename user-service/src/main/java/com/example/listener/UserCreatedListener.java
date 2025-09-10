package com.example.listener;

import com.example.domain.entity.UserProfileDTO;

import com.example.entity.User;
import com.example.service.UserService;
import com.example.util.constant.GenderEnum;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedListener {

    private final UserService userService;

    public UserCreatedListener(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void handleUserCreated(UserProfileDTO profileDTO) {
        // Check phone trùng
        if (userService.isPhoneExist(profileDTO.getPhone())) {
            // Log, gửi event rollback nếu muốn, hoặc báo lỗi
            throw new RuntimeException("Phone " + profileDTO.getPhone() + " đã tồn tại.");
        }

        // Tạo User profile
        User user = new User();
        user.setName(profileDTO.getName());
        user.setEmail(profileDTO.getEmail());
        user.setPhone(profileDTO.getPhone());
        user.setAddress(profileDTO.getAddress());
        user.setDateOfBirth(profileDTO.getDateOfBirth());
        user.setGender(GenderEnum.valueOf(profileDTO.getGender()));
        userService.save(user);
    }
}

