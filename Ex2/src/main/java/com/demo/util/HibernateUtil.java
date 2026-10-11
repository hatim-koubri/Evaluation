package com.demo.util;

import com.demo.classes.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.InputStream;
import java.util.Properties;

public final class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();
    private HibernateUtil() { }

    private static SessionFactory buildSessionFactory() {
        try (InputStream fichier = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (fichier == null) throw new IllegalStateException("application.properties introuvable");
            Properties proprietes = new Properties();
            proprietes.load(fichier);
            return new Configuration().addProperties(proprietes)
                    .addAnnotatedClass(Employe.class).addAnnotatedClass(Projet.class)
                    .addAnnotatedClass(Tache.class).addAnnotatedClass(EmployeTache.class)
                    .buildSessionFactory();
        } catch (Exception erreur) { throw new ExceptionInInitializerError(erreur); }
    }
    public static SessionFactory getSessionFactory() { return SESSION_FACTORY; }
    public static void fermer() { SESSION_FACTORY.close(); }
}