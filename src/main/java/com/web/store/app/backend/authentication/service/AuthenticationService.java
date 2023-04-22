package com.web.store.app.backend.authentication.service;

import com.web.store.app.backend.authentication.dto.AuthenticationRequest;
import com.web.store.app.backend.authentication.dto.AuthenticationResponse;
import com.web.store.app.backend.authentication.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
