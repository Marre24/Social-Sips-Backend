package com.pvt.SocialSips.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.pvt.SocialSips.authentication.FirebaseAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.pvt.SocialSips.constants.WebConstants.*;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public FirebaseAuthenticationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.replace(BEARER_PREFIX, "");
            Optional<String> userId = extractUserIdFromToken(token);

            if (userId.isPresent()) {
                var authentication = new FirebaseAuthenticationToken(userId.get(), null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                setAuthErrorDetails(response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractUserIdFromToken(String token) {
        try {
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token, true);
            String userId = String.valueOf(firebaseToken.getClaims().get(USER_ID_CLAIM));
            return Optional.of(userId);
        } catch (FirebaseAuthException exception) {
            return Optional.empty();
        }
    }

    private void setAuthErrorDetails(HttpServletResponse response) throws IOException {
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
        response.setStatus(unauthorized.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(unauthorized,
                "Authentication failure: Token missing, invalid or expired");
        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }

}
