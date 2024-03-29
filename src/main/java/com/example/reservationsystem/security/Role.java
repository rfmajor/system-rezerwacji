package com.example.reservationsystem.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    USER("USER");

    private final String roleString;

    public static Role fromString(String roleString) {
        return Arrays.stream(values())
                .filter(element -> element.roleString.equals(roleString))
                .findFirst()
                .orElseThrow(() -> new NoSuchRoleException("No such role: " + roleString));
    }
}
