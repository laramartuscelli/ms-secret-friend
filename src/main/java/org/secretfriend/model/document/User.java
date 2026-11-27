package org.secretfriend.model.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.secretfriend.model.dto.SignupDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@Document(collection = "USER")
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public User(SignupDto dto) {
        this(dto.name(), dto.email(), dto.password());
    }
}
