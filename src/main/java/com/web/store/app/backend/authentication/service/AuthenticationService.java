package com.web.store.app.backend.authentication.service;

import com.web.store.app.backend.authentication.dto.AuthenticationRequest;
import com.web.store.app.backend.authentication.dto.AuthenticationResponse;
import com.web.store.app.backend.authentication.dto.RegisterRequest;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

}
