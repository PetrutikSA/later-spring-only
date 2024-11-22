import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.config.PersistenceConfig;
import ru.practicum.config.AppConfig;
import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.mapper.UserMapperImpl;
import ru.practicum.user.model.User;
import ru.practicum.user.UserService;
import ru.practicum.user.UserServiceImpl;
import ru.practicum.user.UserState;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
@SpringJUnitConfig({AppConfig.class, PersistenceConfig.class, UserMapperImpl.class, UserServiceImpl.class})
public class UserServiceImplTests {
    private final EntityManager em;
    private final UserService service;

    @Test
    public void testSaveUser() {
        UserCreateDto userCreateDto = makeUser("some@email.com", "Пётр Иванов");
        service.saveUser(userCreateDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User userFromDb = query.setParameter("email", userCreateDto.getEmail())
                .getSingleResult();

        assertThat(userFromDb.getId(), notNullValue());
        assertThat(userFromDb.getName(), equalTo(userCreateDto.getName()));
        assertThat(userFromDb.getEmail(), equalTo(userCreateDto.getEmail()));
        assertThat(userFromDb.getState(), equalTo(UserState.valueOf(userCreateDto.getState())));
    }

    @Test
    public void testGetUser() {
        UserCreateDto user = makeUser("some1@email.com", "Пётр Иванов1");
        UserCreateDto user2 = makeUser("some2@email.com", "Пётр Иванов2");

        service.saveUser(user);
        service.saveUser(user2);

        List<UserDto> users = service.getAllUsers();
        assertThat(users, notNullValue());

        Map<String, UserDto> userEmailMap = users.stream()
                .collect(Collectors.toMap(UserDto::getEmail, Function.identity()));
        assertThat(userEmailMap.get(user.getEmail()).getName(), equalTo(user.getName()));
        assertThat(userEmailMap.get(user2.getEmail()).getName(), equalTo(user2.getName()));
    }

    private UserCreateDto makeUser(String email, String name) {
        UserCreateDto user = new UserCreateDto();
        user.setEmail(email);
        user.setName(name);
        user.setState("ACTIVE");
        return user;
    }
}
