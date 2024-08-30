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
import ru.practicum.user.User;
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
@TestPropertySource(locations = "classpath:test-application.properties")
@SpringJUnitConfig({AppConfig.class, PersistenceConfig.class, UserServiceImpl.class})
public class UserServiceImplTests {
    private final EntityManager em;
    private final UserService service;

    @Test
    public void testSaveUser() {
        User user = makeUser("some@email.com", "Пётр Иванов");

        service.saveUser(user);
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User userFromDb = query.setParameter("email", user.getEmail())
                .getSingleResult();

        assertThat(userFromDb.getId(), notNullValue());
        assertThat(userFromDb.getName(), equalTo(user.getName()));
        assertThat(userFromDb.getEmail(), equalTo(user.getEmail()));
        assertThat(userFromDb.getState(), equalTo(user.getState()));
    }

    @Test
    public void testGetUser() {
        User user = makeUser("some1@email.com", "Пётр Иванов1");
        User user2 = makeUser("some2@email.com", "Пётр Иванов2");

        service.saveUser(user);
        service.saveUser(user2);

        List<User> users = service.getAllUsers();
        assertThat(users, notNullValue());

        Map<String, User> userEmailMap = users.stream()
                .collect(Collectors.toMap(User::getEmail, Function.identity()));
        assertThat(userEmailMap.get(user.getEmail()).getName(), equalTo(user.getName()));
        assertThat(userEmailMap.get(user2.getEmail()).getName(), equalTo(user2.getName()));
    }

    private User makeUser(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setState(UserState.ACTIVE);
        return user;
    }
}
