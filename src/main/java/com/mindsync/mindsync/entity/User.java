package com.mindsync.mindsync.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@Getter
@Setter
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;
    private String username;
    private String usermbti;
    private String password;

    private String role;

    public String getUserName() {
        return username;
    }
}
