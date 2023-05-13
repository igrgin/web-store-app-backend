package com.web.store.app.backend.user.service;

import com.web.store.app.backend.user.dto.UserProfileDTO;
import com.web.store.app.backend.user.entity.AppUser;
import com.web.store.app.backend.user.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService{

    private final AppUserRepository userRepository;
    @Override
    public Optional<AppUser> findByEmail(String email) {
        return Optional.of(userRepository.findByEmail(email));
    }

    @Override
    public Optional<UserProfileDTO> findByEmailToDTO(String email) {
        return Optional.of(userRepository.findByEmail(email)).map(this::mapAppUserToUserProfile);
    }

    @Override
    public void save(AppUser user) {
        userRepository.save(user);
    }

    private UserProfileDTO mapAppUserToUserProfile(AppUser user) {
        return new UserProfileDTO(user.getId(),user.getFirstName(), user.getLastName(), user.getRole(),user.getEmail());
    }
}
