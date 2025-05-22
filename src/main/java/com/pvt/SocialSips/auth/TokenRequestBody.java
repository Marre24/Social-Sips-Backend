package com.pvt.SocialSips.auth;

import com.pvt.SocialSips.user.User;

public record TokenRequestBody(String accessToken, User user) {
}
