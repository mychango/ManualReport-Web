package com.smb.manualreport.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MRSAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static Logger logger = LoggerFactory.getLogger(MRSAuthenticationFilter.class);
    private String processUrl;

    public MRSAuthenticationFilter(String defaultFilterProcessesUrl, String failureUrl) {
        super(defaultFilterProcessesUrl);
        this.processUrl = defaultFilterProcessesUrl;
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(failureUrl));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if(processUrl.equals(req.getServletPath()) && "POST".equalsIgnoreCase(req.getMethod())){
//            Integer schoolId = Integer.valueOf(req.getParameter("schoolId"));
            String processStep = req.getParameter("process");
            if(processStep != null && !processStep.isEmpty()) {
                req.getSession().setAttribute("processCode", processStep);
                logger.info(">>> " + processStep);
            } else {
                logger.error(">>> Miss process information");
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        return null;
    }
}
