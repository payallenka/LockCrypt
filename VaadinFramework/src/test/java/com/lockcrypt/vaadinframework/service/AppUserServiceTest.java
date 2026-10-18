package com.lockcrypt.vaadinframework.service;

import com.lockcrypt.vaadinframework.model.AppUser;
import com.lockcrypt.vaadinframework.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AppUserServiceTest {

    private final AppUserRepository appUserRepository = mock(AppUserRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AppUserService appUserService = new AppUserService(appUserRepository, passwordEncoder);

    @Test
    void storesOnlyTheHashOfTheLoginPassword() {
        appUserService.register("payal", "longEnough1");

        ArgumentCaptor<AppUser> saved = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(saved.capture());
        assertThat(saved.getValue().getUsername()).isEqualTo("payal");
        assertThat(saved.getValue().getPassword()).isNotEqualTo("longEnough1");
        assertThat(passwordEncoder.matches("longEnough1", saved.getValue().getPassword())).isTrue();
    }

    @Test
    void rejectsTakenUsername() {
        when(appUserRepository.existsByUsername("payal")).thenReturn(true);

        assertThatThrownBy(() -> appUserService.register("payal", "longEnough1"))
                .isInstanceOf(RegistrationException.class)
                .hasMessageContaining("taken");
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void rejectsShortPasswordAndBlankUsername() {
        assertThatThrownBy(() -> appUserService.register("payal", "short"))
                .isInstanceOf(RegistrationException.class);
        assertThatThrownBy(() -> appUserService.register(" ", "longEnough1"))
                .isInstanceOf(RegistrationException.class);
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void loadsExistingUserForLogin() {
        AppUser appUser = new AppUser();
        appUser.setUsername("payal");
        appUser.setPassword(passwordEncoder.encode("longEnough1"));
        when(appUserRepository.findByUsername("payal")).thenReturn(Optional.of(appUser));

        UserDetails userDetails = appUserService.loadUserByUsername("payal");

        assertThat(userDetails.getUsername()).isEqualTo("payal");
        assertThat(userDetails.getAuthorities()).extracting("authority").containsExactly("ROLE_USER");
    }

    @Test
    void unknownUserCannotLogIn() {
        when(appUserRepository.findByUsername("nobody")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appUserService.loadUserByUsername("nobody"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
