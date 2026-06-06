package com.githubanalytics.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            return Map.of("authenticated", false);
        }
        return Map.of(
            "authenticated", true,
            "login",     user.getAttribute("login"),
            "name",      user.getAttribute("name") != null
                             ? user.getAttribute("name")
                             : user.getAttribute("login"),
            "avatarUrl", user.getAttribute("avatar_url")
        );
    }
}