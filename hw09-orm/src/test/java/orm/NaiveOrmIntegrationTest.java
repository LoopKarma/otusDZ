package orm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import orm.entity.Account;
import orm.entity.User;
import orm.executer.DbExecuter;
import orm.repository.Repository;

import java.math.BigDecimal;
import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;

class NaiveOrmIntegrationTest {
    private static final String DB_HOST = "jdbc:h2:mem:";
    private static final String FIRST_USER_NAME = "first one";
    private static final String SECOND_USER_NAME = "second one";
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(DB_HOST);
        connection.setAutoCommit(false);
        createUserTable();
        createAccountTable();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void createUser() {
        Repository dbExecuter = new DbExecuter(connection);

        var firstUser = new User(1, "first one", 20);
        var secondUser = new User(2, "second one", 17);

        dbExecuter.create(firstUser);
        dbExecuter.create(secondUser);

        assertThat(dbExecuter.loadById(0, User.class)).isNull();
        User user1 = dbExecuter.loadById(1, User.class);
        User user2 = dbExecuter.loadById(2, User.class);
        assertThat(dbExecuter.loadById(3, User.class)).isNull();

        assertThat(user1.getId()).isEqualTo(1);
        assertThat(user2.getId()).isEqualTo(2);

        assertThat(user1.getName()).isEqualTo("first one");
        assertThat(user2.getName()).isEqualTo("second one");

        assertThat(user1.getAge()).isEqualTo(20);
        assertThat(user2.getAge()).isEqualTo(17);
    }

    @Test
    void updateUser() throws IllegalAccessException {
        DbExecuter dbExecuter = new DbExecuter(connection);

        int secondAge = 17;
        String updatedName = "updated one";
        var firstUser = new User(1, FIRST_USER_NAME, 20);
        var secondUser = new User(2, SECOND_USER_NAME, secondAge);

        dbExecuter.create(firstUser);
        dbExecuter.create(secondUser);

        secondUser.setName(updatedName);

        dbExecuter.update(secondUser);
        User updatedUser = dbExecuter.loadById(2, User.class);
        assertThat(updatedUser.getAge()).isEqualTo(secondAge);
        assertThat(updatedUser.getName()).isEqualTo(updatedName);
    }

    @Test
    void createAccountItem() throws SQLException {
        DbExecuter dbExecuter = new DbExecuter(connection);
        var first = new Account(1L, "first", new BigDecimal(123));
        var second = new Account(2L, "second", new BigDecimal(124));

        dbExecuter.create(first);
        dbExecuter.create(second);

        assertThat(dbExecuter.loadById(0, Account.class)).isNull();
        var acc1 = dbExecuter.loadById(1L, Account.class);
        var acc2 = dbExecuter.loadById(2L, Account.class);
        assertThat(dbExecuter.loadById(3, Account.class)).isNull();


        assertThat(acc1.getNo()).isEqualTo(1L);
        assertThat(acc2.getNo()).isEqualTo(2L);
        assertThat(acc1.getType()).isEqualTo("first");
        assertThat(acc2.getType()).isEqualTo("second");
        assertThat(acc1.getRest()).isEqualTo(new BigDecimal(123));
        assertThat(acc2.getRest()).isEqualTo(new BigDecimal(124));
    }

    @Test
    void updateAccountItem() throws SQLException, IllegalAccessException {
        DbExecuter dbExecuter = new DbExecuter(connection);
        var first = new Account(1L, "first", new BigDecimal(123));
        var second = new Account(2L, "second", new BigDecimal(124));

        dbExecuter.create(first);
        dbExecuter.create(second);

        first.setType("custom").setRest(new BigDecimal(999));
        dbExecuter.update(first);

        assertThat(first.getNo()).isEqualTo(1L);
        assertThat(first.getType()).isEqualTo("custom");
        assertThat(first.getRest()).isEqualTo(new BigDecimal(999));
    }

    private void createUserTable() throws SQLException {
        String sql = "create table user(id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.executeUpdate();
        }
    }

    private void createAccountTable() throws SQLException {
        String sql = "create table account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.executeUpdate();
        }
    }
}
