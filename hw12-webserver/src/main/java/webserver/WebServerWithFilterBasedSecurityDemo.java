package webserver;

import org.hibernate.SessionFactory;
import webserver.dao.UserDao;
import webserver.dao.UserDaoHibernate;
import webserver.helpers.HibernateUtils;
import webserver.model.User;
import webserver.server.UsersWebServer;
import webserver.server.UsersWebServerWithFilterBasedSecurity;
import webserver.services.TemplateProcessor;
import webserver.services.TemplateProcessorImpl;
import webserver.services.UserAuthService;
import webserver.services.UserAuthServiceImpl;
import webserver.sessionmanager.SessionManagerHibernate;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, userDao, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
