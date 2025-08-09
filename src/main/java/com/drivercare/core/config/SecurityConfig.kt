package com.drivercare.core.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers(AntPathRequestMatcher("/h2-console/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/api/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/ws/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/static/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/index.html")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/css/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/js/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/images/**")).permitAll()
                    .anyRequest().permitAll() // 모든 요청 허용
            }
            .csrf { csrf ->
                csrf.ignoringRequestMatchers(AntPathRequestMatcher("/h2-console/**"))
            }
            .headers { headers ->
                headers.frameOptions().sameOrigin()
            }
            .formLogin { form ->
                form.permitAll()
            }
            .logout { logout ->
                logout.permitAll()
            }

        return http.build()
    }
} 