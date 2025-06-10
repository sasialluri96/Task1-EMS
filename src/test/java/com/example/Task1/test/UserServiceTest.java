package com.example.Task1.test;
import com.example.Task1.DTO.UserDTO;
import com.example.Task1.entities.User;
import com.example.Task1.repo.UserRepo;
import com.example.Task1.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepo userRepo;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testSaveUser_Success() {
        UserDTO dto = new UserDTO();
        dto.setUsername("john");
        dto.setPassword("pass123");
        dto.setRoles("ROLE_USER");
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        User result = userService.saveUser(dto);
        assertNotNull(result);
        assertEquals("john", result.getUsername());
    }

    @Test
    void testSaveUser_MissingUsername() {
        UserDTO dto = new UserDTO();
        dto.setUsername(null);
        dto.setPassword("pass123");
        dto.setRoles("ROLE_USER");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.saveUser(dto));
        assertEquals("Username is required", ex.getMessage());
    }
    @Test
    void testSaveUser_EmptyPassword() {
        UserDTO dto = new UserDTO();
        dto.setUsername("john");
        dto.setPassword("");
        dto.setRoles("ROLE_USER");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.saveUser(dto));
        assertEquals("Password is required", ex.getMessage());
    }


}
