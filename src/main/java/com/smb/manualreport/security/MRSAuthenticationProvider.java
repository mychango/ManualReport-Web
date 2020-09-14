package com.smb.manualreport.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class MRSAuthenticationProvider implements AuthenticationProvider {

    private static Logger logger = LoggerFactory.getLogger(MRSAuthenticationProvider.class);

    @Autowired
    private MRSUserDetailsService mrsUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("Start to authenticate!!");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserDetails details = null;
        try {
            details = mrsUserDetailsService.loadUserByUsername(username);
        } catch (Exception e){
            logger.error("Encounter exception while load user detail information.");
            request.getSession().setAttribute("field", e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        }

//        if (details == null || !(password.equals(details.getPassword()))){
//            logger.error("No user information or wrong password, please check.");
//            request.getSession().setAttribute("field", "error");
//            throw new BadCredentialsException("Password mismtach!");
//        }

        return new UsernamePasswordAuthenticationToken(details, password, details.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
