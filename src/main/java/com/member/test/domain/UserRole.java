package com.member.test.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    // 일반 사용자
    USER("ROLE_USER", "일반 사용자"),

    // 관리자
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;
}
