package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "OTP verification request")
public record OTP(
        @Schema(description = "OTP code sent to email", example = "123456", required = true)
        String OTP,

        @Schema(description = "User ID associated with the OTP", example = "1", required = true)
        Long userId
) {
}