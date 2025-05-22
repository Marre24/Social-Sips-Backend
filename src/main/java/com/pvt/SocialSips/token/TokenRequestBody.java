package com.pvt.SocialSips.token;

import com.pvt.SocialSips.user.User;

public record TokenRequestBody(String accessToken, User user) {
}
