package com.Workspace.goshop.security;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String senha;
}
