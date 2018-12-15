package com.infopulse.service;

import com.infopulse.dto.ChatUserDto;
import com.infopulse.keycloak.KeycloakConnection;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    @Qualifier("broker")
    private KeycloakConnection keycloakConnection;

    @Value("${keycloak.appRealm}")
    private String realm;

    private final static String clientid = "chatservice";

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

         List<ClientRepresentation> clients = keycloakConnection.getKeycloakClient().realm(realm).clients().findByClientId(clientid);
         ClientRepresentation client = clients.get(0);
         List<RoleRepresentation> roles = keycloakConnection.getKeycloakClient().realm(realm).clients().get(client.getId()).roles().list();

         RoleRepresentation role = roles.stream().filter(r -> r.getName().equals("ROLE_USER")).findFirst().get();
         RoleRepresentation roleRepresentation = new RoleRepresentation();
         roleRepresentation.setContainerId(client.getId());
         roleRepresentation.setClientRole(true);
         roleRepresentation.setComposite(false);
         roleRepresentation.setName("ROLE_USER");
         roleRepresentation.setId(role.getId());
         keycloakConnection.getKeycloakClient().realm(realm).users().get(user.getId()).roles().clientLevel(client.getId()).add(Arrays.asList(roleRepresentation));
    }
}
