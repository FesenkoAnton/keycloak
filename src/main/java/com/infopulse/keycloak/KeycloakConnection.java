package com.infopulse.keycloak;

import org.keycloak.admin.client.Keycloak;

public interface KeycloakConnection {
    Keycloak getKeycloakClient();
}
