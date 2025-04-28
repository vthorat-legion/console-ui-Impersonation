package com.legion.okta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.net.util.Base64;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.FormElement;

/**
 * A client to login to a Legion Enterprise via Okta and get the session ID.
 */
public class LegionOktaClient {

    private static final String OKTA_DOMAIN = "legion.okta.com";
    private static final String LEGION_SESSION_ID_HEADER = "sessionId";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Login to a Legion Enterprise via Okta and get the session ID.
     * <p>
     * Note that this assumes there is no MFA (Multi-Factor Authentication) required for the user to login to Okta.
     *
     * @return the session ID of the provided user in the Legion Enterprise.
     */
    public String loginAndGetSessionId(LoginRequest loginRequest) {
        try {
            // Step-1: Login to Okta and Get Okta Session Token
            String oktaSessionToken = getOktaSessionToken(loginRequest);

            // Step-2 Get SAML Assertion
            String samlAssertion = getSamlAssertion(oktaSessionToken, loginRequest.getAppSSOUrl());

            // Step-3 Parse SAMLResponse to Post to Application
            SAMLData samlData = parseSamlData(samlAssertion);

            // Step-4 Post SAMLResponse to Application
            return postSamlResponse(samlData);
        } catch (Exception e) {
            throw new RuntimeException("Login Failed", e);
        }
    }

    /**
     * If user is already logged into Okta and has a session token, this method can be used to trigger SSO to a given
     * enterprise application and get the session ID.
     * <p>
     * This can be used for local testing where the Okta Session Token is available in user's browser session.
     *
     * @param oktaSessionToken a valid session token for Okta session.
     * @param appSSOUrl        the SSO URL of the Okta tile (enterprise) to login to.
     * @return Legion Enterprise session ID.
     */
    public String getSessionId(String oktaSessionToken, String appSSOUrl) {
        try {
            // Step-1 Get SAML Assertion
            String samlAssertion = getSamlAssertion(oktaSessionToken, appSSOUrl);

            // Step-2 Parse SAMLResponse to Post to Application
            SAMLData samlData = parseSamlData(samlAssertion);

            // Step-3 Post SAMLResponse to Application
            return postSamlResponse(samlData);
        } catch (Exception e) {
            throw new RuntimeException("Login Failed", e);
        }
    }

    private static String postSamlResponse(SAMLData samlData) throws IOException, ProtocolException {
        String url = samlData.getBackendUrl();

        List<NameValuePair> nvps =
            Lists.newArrayList(new BasicNameValuePair("SAMLResponse", samlData.getSamlResponse()),
                new BasicNameValuePair("RelayState", samlData.getRelayState()));

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));

        AtomicReference<String> sessionIdReference = new AtomicReference<>("");
        try (CloseableHttpClient httpClient = HttpClients.custom().disableRedirectHandling().build()) {
            httpClient.execute(httpPost, httpResponse -> {
                sessionIdReference.set(httpResponse.getHeader(LEGION_SESSION_ID_HEADER).getValue());
                return httpResponse;
            });
            return sessionIdReference.get();
        }
    }

    private static SAMLData parseSamlData(String samlAssertion) {
        Document doc = Jsoup.parse(samlAssertion);
        FormElement form = (FormElement) doc.getElementById("appForm");
        String backendUrl = form.attr("action");
        String samlResponse = form.getElementsByAttributeValue("name", "SAMLResponse").get(0).val();
        String relayState = form.getElementsByAttributeValue("name", "RelayState").get(0).val();

        return SAMLData.builder().backendUrl(backendUrl).samlResponse(samlResponse).relayState(relayState).build();
    }

    private String getSamlAssertion(String oktaSessionToken, String appSSOUrl) throws IOException {
        String url = appSSOUrl + "?sessionToken=" + oktaSessionToken;
        Request request = Request.get(url).addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json");

        return request.execute().returnContent().asString();
    }

    private String getOktaSessionToken(LoginRequest loginRequest) throws IOException {
        String url = String.format("https://%s/api/v1/authn", OKTA_DOMAIN);
        OktaLoginInput loginInput = OktaLoginInput.builder()
            .username(loginRequest.getUsername())
            .password(new String(Base64.decodeBase64(loginRequest.getBase64EncodedPassword())))
            .loginOptions(
                LoginOptions.builder().warnBeforePasswordExpired(false).multiOptionalFactorEnroll(false).build())
            .build();

        Request request = Request.post(url)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .bodyString(OBJECT_MAPPER.writeValueAsString(loginInput), ContentType.APPLICATION_JSON);

        String resultJson = request.execute().returnContent().asString();
        LoginOutput result = OBJECT_MAPPER.readValue(resultJson, LoginOutput.class);
        return result.getSessionToken();
    }


    @Getter
    @Setter
    @Builder
    private static class LoginOptions {

        private boolean warnBeforePasswordExpired;
        private boolean multiOptionalFactorEnroll;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class LoginOutput {

        private String sessionToken;
        private String expiresAt;
    }

    @Builder
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OktaLoginInput {

        private String username;
        private String password;
        private LoginOptions loginOptions;
    }

    @Getter
    @Builder
    public static class SAMLData {

        private String backendUrl;
        private String samlResponse;
        private String relayState;
    }

}
