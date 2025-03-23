package com.learn.postsmith.config;

import com.learn.postsmith.dto.GeneralResponseDTO;
import com.learn.postsmith.service.UserDetailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain setBasic(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize.requestMatchers("/", "/login", "/api/v1/user/create", "/api/v1/user/login", "/css/**", "/js/**", "/images/**", "/assets/**", "/favicon.ico")
                        .permitAll()
                        .anyRequest()
                        .authenticated()).formLogin(formlogin -> formlogin.loginPage("/login").loginProcessingUrl("/api/v1/user/login")
                        .usernameParameter("email").defaultSuccessUrl("/dashboard")
                        .failureHandler((request, response, exception) -> {
                            response.setHeader("Content-Type", "application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write(new GeneralResponseDTO("error", exception.getMessage(), null, null).generateJson().toString());
                        })).logout((logout) -> {
                    logout.logoutUrl("/api/v1/user/logout").logoutSuccessUrl("/login");
                });
        return httpSecurity.build();
    }

    @Bean
    AuthenticationManager getAuthenticationManager(UserDetailService userDetailService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return new ProviderManager(authProvider);
    }

}
