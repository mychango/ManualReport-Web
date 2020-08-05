package com.smb.manualreport.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class MRSLoginSuccessHandler implements AuthenticationSuccessHandler {

    private static Logger logger = LoggerFactory.getLogger(MRSLoginSuccessHandler.class);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        logger.info("Login successfully and ready to redirect!");
//        Collection<? extends GrantedAuthority> authorities
//                = authentication.getAuthorities();
//        logger.info("Number of authorities: " + authorities.size());
//        for(GrantedAuthority ga : authorities){
//            if(ga.getAuthority().equals("WELD")){
//                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/weldIndex");
//            } else if(ga.getAuthority().equals("BEND")){
//                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/bendIndex");
//            }
//        }
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/index");
    }
}
