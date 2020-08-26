package com.smb.manualreport.security;

import com.smb.manualreport.bean.UserInfo;
import com.smb.manualreport.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Component
public class MRSUserDetailsService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(MRSUserDetailsService.class);

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoService.findUserByUserName(account);
        if(userInfo == null){
            throw new UsernameNotFoundException("Cannot find username:" + account);
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute("userId", userInfo.getUsername());
        request.getSession().setAttribute("nickName", userInfo.getNickname());
        request.getSession().setAttribute("realName", userInfo.getRealname());
        request.getSession().setAttribute("identity", userInfo.getPrivilege());
        request.getSession().setAttribute("area", userInfo.getProcessArea());
        request.getSession().removeAttribute("machineId");

        /*** Authorities 使用方式參考 ***/
        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(() -> userInfo.getProcessArea());
//        if(userInfo.getPrivilege().equals("developer")){
//            authorities.add(() -> "Admin");
//        } else {
//            authorities.add(() -> "Normal");
//        }
        return new User(userInfo.getUsername(), userInfo.getPassword(), authorities);

//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //這邊為了讓錯誤不要跳，再建立UserDetails的時候多加密了一次，應該要做到DB裡的password先加密過
//        return User.withUsername(userInfo.getUsername())
//                .password(passwordEncoder.encode(userInfo.getPassword()))
//                .password(userInfo.getPassword())
//                .roles(userInfo.getPrivilege()).build();
    }
}
