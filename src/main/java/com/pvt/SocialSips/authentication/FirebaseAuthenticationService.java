package com.pvt.SocialSips.authentication;

import com.sun.jdi.request.InvalidRequestStateException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Optional;

import static com.pvt.SocialSips.constants.TokenConstants.*;
import static com.pvt.SocialSips.constants.WebConstants.*;

@Service
public class FirebaseAuthenticationService {

    public record RefreshTokenRequest(String grant_type, String refresh_token) {}
    public record RefreshTokenResponse(String id_token) {}
    public record FirebaseSignInRequest(String email, String password, boolean returnSecureToken) {}
    public record FirebaseSignInResponse(String idToken, String refreshToken) {}

    public String retrieveFirebaseAuth(){
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElseThrow(IllegalStateException::new);
    }

    public RefreshTokenResponse exchangeRefreshToken(String refreshToken) {

        RefreshTokenRequest requestBody = new RefreshTokenRequest(REFRESH_TOKEN_GRANT_TYPE,refreshToken);
        return sendRefreshTokenRequest(requestBody);
    }

    private RefreshTokenResponse sendRefreshTokenRequest(RefreshTokenRequest refreshTokenRequest) {
        try {
            return RestClient.create(REFRESH_TOKEN_BASE_URL)
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam(API_KEY_PARAM, WEB_API_KEY)
                            .build())
                    .body(refreshTokenRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(RefreshTokenResponse.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getResponseBodyAsString().contains(INVALID_REFRESH_TOKEN_ERROR)) {
                throw new AuthenticationServiceException(("Invalid refresh token provided"));
            }
            throw exception;
        }
    }

    private FirebaseSignInResponse sendSignInRequest(FirebaseSignInRequest firebaseSignInRequest) {
        try {
            return RestClient.create(SIGN_IN_BASE_URL)
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam(API_KEY_PARAM, WEB_API_KEY)
                            .build())
                    .body(firebaseSignInRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(FirebaseSignInResponse.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getResponseBodyAsString().contains(INVALID_CREDENTIALS_ERROR)) {
                throw new InvalidRequestStateException("Invalid login credentials provided");
            }
            throw exception;
        }
    }

    // TOTO: Additional token expiration/retrieval/renewal service functions with FirebaseRefreshTokens
}
