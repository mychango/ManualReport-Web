package com.smb.manualreport.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class MRSWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(MRSWebSecurityConfig.class);

    @Autowired
    private MRSLoginSuccessHandler mrsLoginSuccessHandler;

    @Autowired
    private MRSAuthenticationProvider mrsAuthenticationProvider;

    @Autowired
    private MRSUserDetailsService mrsUserDetailsService;

/*** 另一種利用AuthenticationManager套用自定義的AuthenticationProvider的方法 ***/
/*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(mrsAuthenticationProvider);
    }
*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/util/changeSessionLanguage").permitAll()
                .antMatchers("/working/configtest").permitAll()
                .anyRequest().authenticated()
                .and()
                .authenticationProvider(mrsAuthenticationProvider)
                .formLogin().loginPage("/login")
                .failureUrl("/login?error")
//                .defaultSuccessUrl("/test")
                .successHandler(mrsLoginSuccessHandler)
                .and()
                .logout().permitAll()
                .and()
                .sessionManagement().maximumSessions(50)
                .maxSessionsPreventsLogin(false).expiredUrl("/login").sessionRegistry(sessionRegistry());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

/*** 測試登入功能另外一種方式，自行建立imMemory帳號密碼資訊 ***/
/*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth
                .inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("mychango").password(new BCryptPasswordEncoder().encode("admin")).roles("USER");
    }
 */
}
