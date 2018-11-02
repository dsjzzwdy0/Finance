package com.loris.base.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.base.bean.User;
import com.loris.base.repository.mapper.UserMapper;
import com.loris.base.repository.service.UserService;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService
{
}
