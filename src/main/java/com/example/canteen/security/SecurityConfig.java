package com.example.canteen.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomSuccessHandler customSuccessHandler;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(CustomSuccessHandler customSuccessHandler,
                          UserDetailsService userDetailsService) {
        this.customSuccessHandler = customSuccessHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/", "/login", "/signup", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/vendor/**").hasRole("VENDOR")
                        .requestMatchers("/user/**").hasRole("STUDENT")
                        .requestMatchers("/user/cart/**").authenticated()
                        .requestMatchers("/user/menu/**").authenticated()


                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(customSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
