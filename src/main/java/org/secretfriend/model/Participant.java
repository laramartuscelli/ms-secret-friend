package org.secretfriend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class Participant {

    private String name;

    @Setter
    private String secretFriend;

    public Participant(String name) {
        this.name = name;
    }
}
