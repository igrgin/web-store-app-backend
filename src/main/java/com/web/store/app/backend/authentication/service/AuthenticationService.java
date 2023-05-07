package com.web.store.app.backend.authentication.service;

import com.web.store.app.backend.authentication.dto.AuthenticationRequest;
import com.web.store.app.backend.authentication.dto.AuthenticationResponse;
import com.web.store.app.backend.authentication.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public interface AuthenticationService {

    void register(RegisterRequest request);

    Optional<AuthenticationResponse> authenticate(AuthenticationRequest request);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;


}
