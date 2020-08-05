package com.smb.manualreport.service;

import com.smb.manualreport.bean.UserInfo;
import com.smb.manualreport.mapper.UserInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {

    private static Logger logger = LoggerFactory.getLogger(UserInfoService.class);

    @Autowired
    private UserInfoMapper userInfoMapper;

    public List<UserInfo> selectByAll() {
        return userInfoMapper.selectByAll();
    }

    public UserInfo findUserByUserName(String account){
        return userInfoMapper.findUserByUserName(account);
    }
}
