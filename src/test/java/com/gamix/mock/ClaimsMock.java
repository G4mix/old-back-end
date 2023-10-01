package com.gamix.mock;

import org.mockito.Mockito;

import io.jsonwebtoken.Claims;

public class ClaimsMock {
    public static Claims createMockClaims(Integer id, boolean rememberMe) {
        Claims mockClaims = Mockito.mock(Claims.class);
        Mockito.lenient().when(mockClaims.getSubject()).thenReturn(id.toString());
        Mockito.lenient().when(mockClaims.get("rememberMe")).thenReturn(rememberMe);

        return mockClaims;
    }
}
