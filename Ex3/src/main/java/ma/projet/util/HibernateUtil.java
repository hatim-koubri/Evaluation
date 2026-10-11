package ma.projet.util;

import ma.projet.beans.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.InputStream;
import java.util.Properties;

public final class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY = creerSessionFactory();
    private HibernateUtil() { }

    private static SessionFactory creerSessionFactory() {
        try (InputStream fichier = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (fichier == null) throw new IllegalStateException("application.properties introuvable");
            Properties proprietes = new Properties();
            proprietes.load(fichier);
            if ("h2-demo".equals(System.getProperty("app.profile"))) {
                proprietes.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
                proprietes.setProperty("hibernate.connection.url", "jdbc:h2:mem:etatcivil_ex3;DB_CLOSE_DELAY=-1");
                proprietes.setProperty("hibernate.connection.username", "sa");
                proprietes.setProperty("hibernate.connection.password", "");
                proprietes.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
            }
            return new Configuration().addProperties(proprietes)
                    .addAnnotatedClass(Personne.class).addAnnotatedClass(Homme.class)
                    .addAnnotatedClass(Femme.class).addAnnotatedClass(Mariage.class)
                    .buildSessionFactory();
        } catch (Exception erreur) { throw new ExceptionInInitializerError(erreur); }
    }
    public static SessionFactory getSessionFactory() { return SESSION_FACTORY; }
    public static void fermer() { SESSION_FACTORY.close(); }
}