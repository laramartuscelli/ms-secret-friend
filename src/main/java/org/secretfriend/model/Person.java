package org.secretfriend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class Person {

    private String name;

    @Setter
    private String secretFriend;

    public Person(String name) {
        this.name = name;
    }
}
