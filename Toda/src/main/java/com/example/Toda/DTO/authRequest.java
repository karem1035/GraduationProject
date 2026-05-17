package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request body for user authentication (login)")
public class authRequest {

    @Schema(description = "User email address", example = "john.doe@example.com", required = true)
    private String email;

    @Schema(description = "User password", example = "MySecurePass123!", required = true)
    private String password;
}