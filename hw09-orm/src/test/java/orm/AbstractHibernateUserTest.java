package orm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import orm.entity.*;
import orm.hibernate.HibernateUtils;

public abstract class AbstractHibernateUserTest {
    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate-test.cfg.xml";

    protected static final String FIELD_ID = "id";
    protected static final String FIELD_NAME = "name";
    protected static final String TEST_USER_NAME = "Вася";
    protected static final String TEST_USER_NEW_NAME = "НЕ Вася";
    protected static final int TEST_USER_AGE_0 = 17;
    protected static final int TEST_USER_AGE_1 = 20;
    protected static final String TEST_ADDRESS = "street 1";
    protected static final String TEST_PHONE_1 = "123-123-123-123";
    protected static final String TEST_PHONE_2 = "+900-082-2132";


    protected SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory(
                HIBERNATE_CFG_XML_FILE_RESOURCE,
                User.class,
                PhoneDataSet.class,
                AddressDataSet.class
        );
    }

    protected User buildDefaultUser() {
        var user = new User(0, TEST_USER_NAME, TEST_USER_AGE_0);
        user.setAddress(new AddressDataSet(TEST_ADDRESS));
        user.addPhone(new PhoneDataSet(TEST_PHONE_1, user));
        user.addPhone(new PhoneDataSet(TEST_PHONE_2, user));

        return user;
    }

    protected void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            saveUser(session, user);
        }
    }

    protected void saveUser(Session session, User user) {
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }

    protected User loadUser(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(User.class, id);
        }
    }

    protected EntityStatistics getUserStatistics() {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(User.class.getName());
    }
}
