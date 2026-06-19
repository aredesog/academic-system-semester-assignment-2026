package org.example.academic.system.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "username")
public class User {

    private String username;
    private String password;
    private Role role;

}
