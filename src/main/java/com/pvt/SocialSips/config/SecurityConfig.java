package com.pvt.SocialSips.config;

import com.pvt.SocialSips.user.OidcUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    private final OidcUserDetailsService oidcUserDetailsService;
    private final AuthenticationSuccessHandlerConfig successHandlerConfig;

    @Autowired
    public SecurityConfig(OidcUserDetailsService oidcUserDetailsService, AuthenticationSuccessHandlerConfig successHandlerConfig) {
        this.oidcUserDetailsService = oidcUserDetailsService;
        this.successHandlerConfig = successHandlerConfig;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/home", "/event/**", "/questpool/**",
                                "/css/**", "/h2-console", "/error", "/h2-console/**").permitAll()
                        .requestMatchers("/user/profile").authenticated()

                )
                .headers(h -> h
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(cfg -> cfg
                        .defaultSuccessUrl("/user/profile")
                        .userInfoEndpoint(custom -> custom.oidcUserService(oidcUserDetailsService))
                        .successHandler(authenticationSuccessHandler()))
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return successHandlerConfig;
    }

    @Bean
    public RoleHierarchy roleHierarchy(){
        return RoleHierarchyImpl.fromHierarchy("ADMIN > HOST > OIDC_USER > GUEST");
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(oidcUserDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityExpressionHandler<FilterInvocation> webExpressionHandler(){
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setDefaultRolePrefix("ROLE_");
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }
}
