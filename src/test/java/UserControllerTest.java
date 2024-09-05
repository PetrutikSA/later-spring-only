import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.UserController;
import ru.practicum.user.UserService;
import ru.practicum.user.UserState;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mvc;

    private UserDto user;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new UserDto();
        user.setName("Name");
        user.setEmail("email@ya.ru");
        user.setId(1L);
        user.setState(UserState.ACTIVE);
    }

    @Test
    void saveNewUser() throws Exception {
        when(userService.saveUser(any()))
                .thenReturn(user);

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(user))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(user.getEmail()),String.class))
                .andExpect(jsonPath("$.name", is(user.getName()), String.class));
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(List.of(user));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$[0].email", is(user.getEmail()),String.class))
                .andExpect(jsonPath("$[0].name", is(user.getName()), String.class));
    }
}
