package orm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import orm.dao.UserDao;
import orm.entity.User;
import orm.hibernate.dao.UserDaoHibernate;
import orm.hibernate.sessionmanager.SessionManagerHibernate;
import orm.service.DbServiceUser;
import orm.service.DbServiceUserImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Демо работы с hibernate (с абстракциями) должно ")
public class WithAbstractionsTest extends AbstractHibernateUserTest {
    private DbServiceUser dbServiceUser;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        dbServiceUser = new DbServiceUserImpl(userDao);
    }

    @Test
    @DisplayName(" корректно сохранять пользователя")
    void shouldCorrectSaveUser() {
        var user = buildDefaultUser();

        User savedUser = dbServiceUser.saveUser(user);
        User loadedUser = loadUser(savedUser.getId());
        assertThat(loadedUser).isEqualToComparingFieldByFieldRecursively(savedUser);

        System.out.println(savedUser);
        System.out.println(loadedUser);
    }

    @Test
    @DisplayName(" корректно загружать пользователя")
    void shouldLoadCorrectUser(){
        var user = buildDefaultUser();
        saveUser(user);

        Optional<User> mayBeUser = dbServiceUser.getUser(user.getId());

        assertThat(mayBeUser).isPresent().get().isEqualToComparingFieldByFieldRecursively(user);

        System.out.println(user);
        mayBeUser.ifPresent(System.out::println);
    }

    @Test
    @DisplayName(" корректно изменять ранее сохраненного пользователя")
    void shouldCorrectUpdateSavedUser(){
        User savedUser = buildDefaultUser();
        saveUser(savedUser);

        User savedUser2 = new User(savedUser.getId(), TEST_USER_NEW_NAME, TEST_USER_AGE_1);
        long id = dbServiceUser.updateUser(savedUser2).getId();
        User loadedUser = loadUser(id);

        assertThat(loadedUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue(FIELD_ID, id)
                .isEqualToComparingOnlyGivenFields(savedUser2, FIELD_NAME);

        System.out.println(savedUser);
        System.out.println(savedUser2);
        System.out.println(loadedUser);
    }
}
