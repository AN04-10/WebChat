package com.webChat.config;

import com.webChat.config.filter.TokenVerifyFilter;
import com.webChat.config.handler.MyAccessDeniedHandler;
import com.webChat.config.handler.MyAuthenticationFailureHandler;
import com.webChat.config.handler.MyAuthenticationSuccessHandler;
import com.webChat.config.handler.MyLogoutSuccessHandler;
import com.webChat.constant.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.Arrays;

@EnableMethodSecurity // 开启方法级别的权限检查
@Configuration
public class SecurityConfig {

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Resource
    private TokenVerifyFilter tokenVerifyFilter;

    @Resource
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;

    //There is no PasswordEncoder mapped for the id "null"
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //禁用跨站请求伪造
        return httpSecurity
                .formLogin((formLogin) -> {
                    formLogin.loginProcessingUrl(Constants.LOGIN_URI)
                            .usernameParameter("loginAct")
                            .passwordParameter("loginPwd")
                            .successHandler(myAuthenticationSuccessHandler)
                            .failureHandler(myAuthenticationFailureHandler);
                })
                .authorizeHttpRequests((authorize) -> {
                    authorize
                            .requestMatchers("/api/login").permitAll() //开放请求
                            .anyRequest().authenticated(); //拦截请求
                })
                .csrf(AbstractHttpConfigurer::disable) //禁用跨站请求伪造
                .cors((cors) -> {
                    cors.configurationSource(configurationSource());
                })
                .sessionManagement((session) -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(tokenVerifyFilter, LogoutFilter.class)
                .logout((logout) -> {
                    logout.logoutUrl("/api/logout")
                            .logoutSuccessHandler(myLogoutSuccessHandler);
                })
                .exceptionHandling((exceptionHandling) -> {
                    exceptionHandling.accessDeniedHandler(myAccessDeniedHandler);
                })
                .build();
    }

    /**
     * 跨域设置
     *
     * @return
     */
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); //允许任何来源，http://localhost:8081
        configuration.setAllowedMethods(Arrays.asList("*")); //允许任何请求方法，post、get、put、delete
        configuration.setAllowedHeaders(Arrays.asList("*")); //允许任何的请求头

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
