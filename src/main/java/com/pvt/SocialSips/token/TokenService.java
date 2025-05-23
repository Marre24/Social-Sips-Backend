package com.pvt.SocialSips.token;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.pvt.SocialSips.user.User;
import com.pvt.SocialSips.user.UserService;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    private final GoogleIdTokenVerifier verifier;

    private final UserService userService;

    public TokenService(JwtEncoder encoder, JwtDecoder decoder, GoogleIdTokenVerifier verifier, UserService userService){
        this.encoder = encoder;
        this.decoder = decoder;
        this.verifier = verifier;
        this.userService = userService;
    }

    public String generateToken(User user){
        User userInDatabase = userService.getOrCreateUser(user);

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(userInDatabase.getSub())
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public GoogleIdToken.Payload verifyGoogleIdToken(String googleIdToken){
        try{
            GoogleIdToken verifiedToken = verifier.verify(googleIdToken);
            return verifiedToken != null ? verifiedToken.getPayload() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
