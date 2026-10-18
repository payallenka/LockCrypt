package com.lockcrypt.mongodbdatabase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lockcrypt.mongodbdatabase.configuration.InternalTokenFilter;
import com.lockcrypt.mongodbdatabase.model.UserData;
import com.lockcrypt.mongodbdatabase.model.UserDataRequest;
import com.lockcrypt.mongodbdatabase.service.UserDataNotFoundException;
import com.lockcrypt.mongodbdatabase.service.UserDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserDataController.class)
@TestPropertySource(properties = "lockcrypt.internal-token=test-token")
class UserDataControllerTest {

    private static final String TOKEN = "test-token";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDataService userDataService;

    @Test
    void returnsOnlyTheOwnersData() throws Exception {
        UserData userData = new UserData();
        userData.setId("abc");
        userData.setOwner("payal");
        userData.setAccountFor("github");
        when(userDataService.getUserData("payal")).thenReturn(List.of(userData));

        mockMvc.perform(get("/userData").param("owner", "payal").header(InternalTokenFilter.HEADER, TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountFor").value("github"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void savesValidUserData() throws Exception {
        UserData saved = new UserData();
        saved.setId("abc");
        when(userDataService.saveUserData(any(UserDataRequest.class))).thenReturn(saved);

        UserDataRequest request = new UserDataRequest("payal", "github", "payal-dev", "bWFza2VkLXZhbHVl", 1);

        mockMvc.perform(post("/userData")
                        .header(InternalTokenFilter.HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("abc"));
    }

    @Test
    void rejectsUserDataWithMissingFields() throws Exception {
        mockMvc.perform(post("/userData")
                        .header(InternalTokenFilter.HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"owner":"payal","accountFor":"","username":"payal"}
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userDataService);
    }

    @Test
    void deletingSomeoneElsesDataReturnsNotFound() throws Exception {
        doThrow(new UserDataNotFoundException("abc")).when(userDataService).deleteUserData("abc", "someoneElse");

        mockMvc.perform(delete("/userData/abc").param("owner", "someoneElse").header(InternalTokenFilter.HEADER, TOKEN))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsRequestsWithoutTheInternalToken() throws Exception {
        mockMvc.perform(get("/userData").param("owner", "payal"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/userData").param("owner", "payal").header(InternalTokenFilter.HEADER, "wrong"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(userDataService);
    }
}
