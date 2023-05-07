package com.web.store.app.backend.user.service;

import com.web.store.app.backend.user.entity.AppUser;

import java.util.Optional;

public interface AppUserService {

    Optional<AppUser> findByEmail(String email);

    void save(AppUser user);
}
