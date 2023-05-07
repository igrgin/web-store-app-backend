package com.web.store.app.backend.user.service;

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
    public void save(AppUser user) {
        userRepository.save(user);
    }
}
