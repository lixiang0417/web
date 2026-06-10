package com.stu.helloserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stu.helloserver.common.Result;
import com.stu.helloserver.dto.UserDTO;
import com.stu.helloserver.entity.UserInfo;
import com.stu.helloserver.vo.UserDetailVO;

public interface UserService {
    Result<String> register(UserDTO userDTO);
    Result<String> login(UserDTO userDTO);
    Result<String> getUserById(Long id);
    Result<Object> getUserPage(Integer pageNum, Integer pageSize);

    // 这行是关键：补全 detail 接口声明
    Result<UserDetailVO> getUserDetail(Long userId);

    Result<String> updateUserInfo(UserInfo userInfo);
    Result<String> deleteUser(Long userId);
}