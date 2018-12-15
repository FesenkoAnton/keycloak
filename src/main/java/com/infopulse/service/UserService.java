package com.infopulse.service;

import com.infopulse.dto.ChatUserDto;
import com.infopulse.keycloak.KeycloakConnection;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    @Qualifier("broker")
    private KeycloakConnection keycloakConnection;

    @Value("${keycloak.appRealm}")
    private String realm;

    public void insert(ChatUserDto chatUserDto){
         UserRepresentation userRepresentation =new UserRepresentation();
         userRepresentation.setLastName(chatUserDto.getName());
         userRepresentation.setEnabled(true);
         userRepresentation.setUsername(chatUserDto.getLogin());
         keycloakConnection.getKeycloakClient().realm(realm).users().create(userRepresentation);

         List<UserRepresentation> users = keycloakConnection.getKeycloakClient().realm(realm).users().search(chatUserDto.getLogin());
         UserRepresentation user = users.get(0);
         CredentialRepresentation credentialRepresentation =new CredentialRepresentation();
         credentialRepresentation.setTemporary(false);
         credentialRepresentation.setType("password");
         credentialRepresentation.setValue(chatUserDto.getPassword());
         keycloakConnection.getKeycloakClient().realm(realm).users().get(user.getId()).resetPassword(credentialRepresentation);

    }
}
