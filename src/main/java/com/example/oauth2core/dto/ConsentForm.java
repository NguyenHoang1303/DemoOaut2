package com.example.oauth2core.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsentForm {
    private String clientId;
    private String scope;
    private String username;
    private String redirectUri;


    public static final class ConsentFormBuilder {
        private String clientId;
        private String scope;
        private String username;
        private String redirectUri;

        private ConsentFormBuilder() {
        }

        public static ConsentFormBuilder aConsentForm() {
            return new ConsentFormBuilder();
        }

        public ConsentFormBuilder withClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public ConsentFormBuilder withScope(String scope) {
            this.scope = scope;
            return this;
        }

        public ConsentFormBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public ConsentFormBuilder withRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public ConsentForm build() {
            ConsentForm consentForm = new ConsentForm();
            consentForm.setClientId(clientId);
            consentForm.setScope(scope);
            consentForm.setUsername(username);
            consentForm.setRedirectUri(redirectUri);
            return consentForm;
        }
    }
}
