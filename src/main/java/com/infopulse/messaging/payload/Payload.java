package com.infopulse.messaging.payload;

import com.infopulse.dto.ChatUserDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Payload {
    private ChatUserDto sendObject;
    private String event;
}
