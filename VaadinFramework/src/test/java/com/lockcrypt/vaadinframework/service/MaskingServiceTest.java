package com.lockcrypt.vaadinframework.service;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MaskingServiceTest {

    private final MaskingService maskingService = new MaskingService();

    @Test
    void decryptsBackToTheOriginalPassword() {
        String key = maskingService.generateKey();
        String maskedPassword = maskingService.encrypt("My$ecretPassw0rd", key);

        assertThat(maskedPassword).doesNotContain("My$ecretPassw0rd");
        assertThat(maskingService.decryptPassword(key, maskedPassword)).isEqualTo("My$ecretPassw0rd");
    }

    @Test
    void worksForPasswordsOfAnyLength() {
        String key = maskingService.generateKey();

        assertThat(maskingService.decryptPassword(key, maskingService.encrypt("a", key))).isEqualTo("a");
        String longPassword = "x".repeat(200);
        assertThat(maskingService.decryptPassword(key, maskingService.encrypt(longPassword, key))).isEqualTo(longPassword);
    }

    @Test
    void samePasswordEncryptsDifferentlyEveryTime() {
        String key = maskingService.generateKey();

        assertThat(maskingService.encrypt("password", key)).isNotEqualTo(maskingService.encrypt("password", key));
    }

    @Test
    void generatesA256BitKey() {
        assertThat(Base64.getDecoder().decode(maskingService.generateKey())).hasSize(32);
        assertThat(maskingService.generateKey()).isNotEqualTo(maskingService.generateKey());
    }

    @Test
    void failsWithTheWrongKey() {
        String maskedPassword = maskingService.encrypt("password", maskingService.generateKey());

        assertThatThrownBy(() -> maskingService.decryptPassword(maskingService.generateKey(), maskedPassword))
                .isInstanceOf(MaskingException.class);
    }

    @Test
    void failsWhenTheMaskedPasswordWasTamperedWith() {
        String key = maskingService.generateKey();
        byte[] masked = Base64.getDecoder().decode(maskingService.encrypt("password", key));
        masked[masked.length - 1] ^= 1;
        String tampered = Base64.getEncoder().encodeToString(masked);

        assertThatThrownBy(() -> maskingService.decryptPassword(key, tampered))
                .isInstanceOf(MaskingException.class);
    }

    @Test
    void failsOnGarbageInput() {
        String key = maskingService.generateKey();

        assertThatThrownBy(() -> maskingService.decryptPassword(key, "not base64 !!"))
                .isInstanceOf(MaskingException.class);
        assertThatThrownBy(() -> maskingService.decryptPassword(key, "YWJj"))
                .isInstanceOf(MaskingException.class);
    }
}
