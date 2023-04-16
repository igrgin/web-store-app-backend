package com.web.store.app.service;

import com.web.store.app.dto.AuthenticationRequest;
import com.web.store.app.dto.AuthenticationResponse;
import com.web.store.app.dto.RegisterRequest;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

}
