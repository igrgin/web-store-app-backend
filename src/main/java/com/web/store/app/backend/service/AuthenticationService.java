package com.web.store.app.backend.service;

import com.web.store.app.backend.dto.AuthenticationRequest;
import com.web.store.app.backend.dto.AuthenticationResponse;
import com.web.store.app.backend.dto.RegisterRequest;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

}
