package com.mindsync.mindsync.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "refresh")
public class Refresh {

    @Id
    private String id;

    private String email;
    private String refresh;
    private String expiration;
}
