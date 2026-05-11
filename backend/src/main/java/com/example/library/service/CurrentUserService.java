package com.example.library.service;

import com.example.library.entity.User;
import com.example.library.exception.BusinessException;
import com.example.library.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("请先登录");
        }
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new BusinessException("当前用户不存在"));
    }
}
