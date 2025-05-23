package com.pvt.SocialSips.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.pvt.SocialSips.constants.WebConstants.*;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/**")
                .requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, PERMITTED_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, PERMITTED_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, PERMITTED_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST,PROTECTED_ENDPOINTS).hasRole("USER")
                        .requestMatchers(HttpMethod.GET, PROTECTED_ENDPOINTS).hasRole("USER")
                        .requestMatchers(HttpMethod.OPTIONS, PROTECTED_ENDPOINTS).hasRole("USER"))
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public  Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        return converter;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(ALLOWED_ORIGINS));
        configuration.setAllowedMethods(List.of(ALLOWED_METHODS));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                Map<String, Object> claims = decodedToken.getClaims();

                Instant now = Instant.now();
                Instant expiresAt = now.plus(7, ChronoUnit.DAYS);

                return Jwt.withTokenValue(token)
                        .header("alg", "RS256")
                        .issuer("https://securetoken.google.com/social-sips-ec954")
                        .subject(decodedToken.getUid())
                        .claim("name", decodedToken.getName())
                        .claim("authorities", List.of("ROLE_USER"))
                        .claim("scope", "openid")
                        .claim("roles", "ROLE_USER")
                        .issuedAt(now)
                        .expiresAt(expiresAt)
                        .build();
            } catch (FirebaseAuthException e) {
                throw new BadCredentialsException("Invalid token", e);
            }
        };
    }

    @Bean
    public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter(){
        return new JwtGrantedAuthoritiesConverter();
    }

}
