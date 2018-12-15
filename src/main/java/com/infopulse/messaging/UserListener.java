package com.infopulse.messaging;

import com.infopulse.dto.ChatUserDto;
import com.infopulse.messaging.payload.Payload;
import com.infopulse.service.UserService;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(UserInput.class)
@Setter
@NoArgsConstructor
public class UserListener {

    @Autowired
    private UserService userService;

    @StreamListener(target = UserInput.INPUT)
    public void onTenantEvent(Message<Payload> message) {

        Payload event = message.getPayload();
        ChatUserDto user = event.getSendObject();
        switch (event.getEvent()) {
            case "INSERT":
                userService.insert(user);
                break;
            default:
                break;
        }
    }
}
