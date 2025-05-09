package com.pvt.SocialSips.config;

import com.pvt.SocialSips.user.OidcUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

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
                        .requestMatchers("/", "/home", "/css/**", "/error").permitAll()
                        .requestMatchers("/event/join/**", "/questpool/**").permitAll()
                        .requestMatchers("/user/**", "/event/", "/event/start/").authenticated()
                )
                .addFilterBefore(new FirebaseAuthenticationFilter(), BasicAuthenticationFilter.class)
                .requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .oauth2Login(cfg -> cfg
                        .defaultSuccessUrl("/user/profile")
                        .userInfoEndpoint(custom -> custom.oidcUserService(oidcUserDetailsService))
                        .successHandler(authenticationSuccessHandler()))
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/questpool/**")
                .allowedOrigins("https://social-sips-ec954.web.app")
                .allowedOrigins("https://social-sips-ec954.firebaseapp.com")
                .allowedMethods("GET")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        final FilterRegistrationBean<ForwardedHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new ForwardedHeaderFilter());
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return successHandlerConfig;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("ADMIN > HOST > OIDC_USER > GUEST");
    }

    @Bean
    public SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setDefaultRolePrefix("ROLE_");
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }
}
