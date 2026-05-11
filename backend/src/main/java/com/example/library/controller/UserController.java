package com.example.library.controller;

import com.example.library.common.ApiResponse;
import com.example.library.common.PageResponse;
import com.example.library.entity.User;
import com.example.library.repository.UserRepository;
import com.example.library.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ApiResponse<PageResponse<UserVO>> page(@RequestParam(required = false) String keyword,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size);
        Page<User> userPage;
        if (keyword == null || keyword.trim().isEmpty()) {
            userPage = userRepository.findAll(pageRequest);
        } else {
            userPage = userRepository.findByUsernameContainingOrRealNameContaining(keyword, keyword, pageRequest);
        }
        List<UserVO> records = userPage.getContent().stream().map(UserVO::from).collect(Collectors.toList());
        return ApiResponse.ok(PageResponse.from(new PageImpl<UserVO>(records, pageRequest, userPage.getTotalElements())));
    }

    @PostMapping
    public ApiResponse<UserVO> create(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        return ApiResponse.ok(UserVO.from(userRepository.save(user)));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<UserVO> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        user.setStatus(status);
        return ApiResponse.ok(UserVO.from(userRepository.save(user)));
    }
}
