package com.smb.manualreport.mapper;

import com.smb.manualreport.bean.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserInfoMapper {

    @Select("select * from smb_op.user_info")
    List<UserInfo> selectByAll();

    @Select("select * from smb_op.user_info where username = #{username}")
    UserInfo findUserByUserName(@Param("username") String account);
}
