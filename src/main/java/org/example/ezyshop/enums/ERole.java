package org.example.ezyshop.enums;

import lombok.Getter;

@Getter
public enum ERole {
    ROLE_USER("user"),
    ROLE_ADMIN("admin");

    String text;

    ERole(String text) {
        this.text = text;
    }
}