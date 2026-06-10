package com.stu.helloserver.controller;

import com.stu.helloserver.common.Result;
import com.stu.helloserver.dto.UserDTO;
import com.stu.helloserver.entity.UserInfo;
import com.stu.helloserver.service.UserService;
import com.stu.helloserver.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Result register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/page")
    public Result getUserPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize
    ) {
        return userService.getUserPage(pageNum, pageSize);
    }

    @GetMapping("/{id}/detail")
    public Result<UserDetailVO> getUserDetail(@PathVariable Long id) {
        return userService.getUserDetail(id);
    }

    @PutMapping("/{id}/info")
    public Result updateUserInfo(@PathVariable Long id, @RequestBody UserInfo userInfo) {
        userInfo.setUserId(id);
        return userService.updateUserInfo(userInfo);
    }

    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}