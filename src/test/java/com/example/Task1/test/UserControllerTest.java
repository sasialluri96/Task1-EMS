package com.example.Task1.test;
import com.example.Task1.DTO.UserDTO;
import com.example.Task1.controller.UserController;
import com.example.Task1.entities.User;
import com.example.Task1.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUser_Success() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setUsername("john");
        dto.setPassword("pass123");
        dto.setRoles("ROLE_USER");
        User savedUser = new User();
        savedUser.setUsername("john");
        savedUser.setPassword("pass123");
        savedUser.setRoles("ROLE_USER");
        when(userService.saveUser(any(UserDTO.class))).thenReturn(savedUser);
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("john"));
    }
    @Test
    void testCreateUser_MissingUsername() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setUsername(null);
        dto.setPassword("pass123");
        dto.setRoles("ROLE_USER");
        when(userService.saveUser(any(UserDTO.class))).thenThrow(new IllegalArgumentException("Username is required"));
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("Username is required", result.getResolvedException().getMessage()));
    }
    @Test
    void testCreateUser_EmptyPassword() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setUsername("john");
        dto.setPassword("");
        dto.setRoles("ROLE_USER");
        when(userService.saveUser(any(UserDTO.class))).thenThrow(new IllegalArgumentException("Password is required"));
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("Password is required", result.getResolvedException().getMessage()));
    }
}