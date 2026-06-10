package com.stu.helloserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stu.helloserver.common.Result;
import com.stu.helloserver.dto.UserDTO;
import com.stu.helloserver.entity.User;
import com.stu.helloserver.entity.UserInfo;
import com.stu.helloserver.mapper.UserMapper;
import com.stu.helloserver.mapper.UserInfoMapper;
import com.stu.helloserver.service.UserService;
import com.stu.helloserver.utils.JwtUtils;
import com.stu.helloserver.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Result<String> register(UserDTO userDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        User exist = userMapper.selectOne(wrapper);

        if (exist != null) {
            return Result.error("用户名已存在");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        userMapper.insert(user);

        return Result.success("注册成功");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            return Result.error("用户不存在");
        }

        if (!user.getPassword().equals(userDTO.getPassword())) {
            return Result.error("密码错误");
        }

        String token = jwtUtils.generateToken(user.getUsername());
        return Result.success("Bearer " + token);
    }

    @Override
    public Result<String> getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success("查询成功");
    }

    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        try {
            if (pageNum == null || pageNum < 1) pageNum = 1;
            if (pageSize == null || pageSize < 1) pageSize = 5;

            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(User::getId, User::getUsername, User::getPassword);

            Page<User> page = new Page<>(pageNum, pageSize);
            Page<User> userPage = userMapper.selectPage(page, wrapper);

            // 绑定 user_info
            List<UserInfo> infoList = userInfoMapper.selectList(null);
            Map<Long, UserInfo> infoMap = infoList.stream()
                    .collect(Collectors.toMap(UserInfo::getUserId, i -> i));

            for (User user : userPage.getRecords()) {
                user.setUserInfo(infoMap.get(user.getId()));
            }

            return Result.success(userPage);

        } catch (Exception e) {
            return Result.error("SQL错误：" + e.getMessage());
        }
    }

    @Override
    public Result<UserDetailVO> getUserDetail(Long userId) {
        // 暂时去掉缓存逻辑，只保留原代码
        UserDetailVO detail = userInfoMapper.getUserDetail(userId);
        if (detail == null) {
            return Result.error("用户不存在");
        }
        return Result.success(detail);
    }

    @Override
    public Result<String> updateUserInfo(UserInfo userInfo) {
        // 参数校验
        if (userInfo == null || userInfo.getUserId() == null) {
            return Result.error("参数错误");
        }

        // 更新数据库
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserId, userInfo.getUserId());
        userInfoMapper.update(userInfo, wrapper);

        return Result.success("更新成功");
    }

    @Override
    @Transactional
    public Result<String> deleteUser(Long userId) {
        try {
            // 1. 删除用户信息（从表）
            LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserInfo::getUserId, userId);
            userInfoMapper.delete(wrapper);

            // 2. 删除用户（主表）
            int rows = userMapper.deleteById(userId);
            if (rows > 0) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败，用户不存在");
            }
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}