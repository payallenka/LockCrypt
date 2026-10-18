package com.lockcrypt.mysqlhandle.controller;

import com.lockcrypt.mysqlhandle.configuration.InternalTokenFilter;
import com.lockcrypt.mysqlhandle.model.Password;
import com.lockcrypt.mysqlhandle.model.PasswordKeyRequest;
import com.lockcrypt.mysqlhandle.service.KeyNotFoundException;
import com.lockcrypt.mysqlhandle.service.PasswordKeyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PasswordController.class)
@TestPropertySource(properties = "lockcrypt.internal-token=test-token")
class PasswordControllerTest {

    private static final String TOKEN = "test-token";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordKeyService passwordKeyService;

    @Test
    void returnsTheOwnersKeys() throws Exception {
        Password key = new Password();
        key.setId(7);
        key.setOwner("payal");
        key.setPasswordKey("a2V5");
        when(passwordKeyService.getKeys("payal")).thenReturn(List.of(key));

        mockMvc.perform(get("/keys").param("owner", "payal").header(InternalTokenFilter.HEADER, TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(7))
                .andExpect(jsonPath("$[0].passwordKey").value("a2V5"));
    }

    @Test
    void savesKeyAndReturnsItsId() throws Exception {
        Password saved = new Password();
        saved.setId(7);
        when(passwordKeyService.saveKey(new PasswordKeyRequest("payal", "a2V5"))).thenReturn(saved);

        mockMvc.perform(post("/keys")
                        .header(InternalTokenFilter.HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"owner":"payal","passwordKey":"a2V5"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void rejectsBlankKey() throws Exception {
        mockMvc.perform(post("/keys")
                        .header(InternalTokenFilter.HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"owner":"payal","passwordKey":" "}
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(passwordKeyService);
    }

    @Test
    void deletingSomeoneElsesKeyReturnsNotFound() throws Exception {
        doThrow(new KeyNotFoundException(7)).when(passwordKeyService).deleteKey(7, "someoneElse");

        mockMvc.perform(delete("/keys/7").param("owner", "someoneElse").header(InternalTokenFilter.HEADER, TOKEN))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsRequestsWithoutTheInternalToken() throws Exception {
        mockMvc.perform(get("/keys").param("owner", "payal"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(passwordKeyService);
    }
}
