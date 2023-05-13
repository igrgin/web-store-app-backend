package com.web.store.app.backend.user.service;

import com.web.store.app.backend.user.dto.UserProfileDTO;
import com.web.store.app.backend.user.entity.AppUser;

import java.util.Optional;

public interface AppUserService {

    Optional<AppUser> findByEmail(String email);

    Optional<UserProfileDTO> findByEmailToDTO(String email);

    void save(AppUser user);
}
