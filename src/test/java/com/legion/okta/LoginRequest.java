package com.legion.okta;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginRequest {

    // The username to login to Okta. E.g., xyz@legion.co
    private String username;
    // Base64 encoded password to login to Okta.
    private String base64EncodedPassword;
    // Okta SSO URL for the app; e.g., https://legion.okta.com/app/legionorg820553_legioncoffeestaging_1/exk1jx94w26nST3ji357/sso/saml
    private String appSSOUrl;
}
