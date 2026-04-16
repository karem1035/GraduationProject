package com.example.Toda.DTO;

public record updateProfileRequest(
        String firstName,
        String lastName,
        String email,
        String phone

) {
}
