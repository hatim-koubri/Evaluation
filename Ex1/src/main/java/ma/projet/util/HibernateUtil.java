package ma.projet.util;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.Properties;

public final class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY = construireSessionFactory();

    private HibernateUtil() { }

    private static SessionFactory construireSessionFactory() {
        try (InputStream flux = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (flux == null) throw new IllegalStateException("application.properties introuvable");
            Properties reglages = new Properties();
            reglages.load(flux);
            return new Configuration()
                    .setProperties(reglages)
                    .addAnnotatedClass(Categorie.class)
                    .addAnnotatedClass(Produit.class)
                    .addAnnotatedClass(Commande.class)
                    .addAnnotatedClass(LigneCommandeProduit.class)
                    .buildSessionFactory();
        } catch (Exception erreur) {
            throw new ExceptionInInitializerError(erreur);
        }
    }

    public static SessionFactory getSessionFactory() { return SESSION_FACTORY; }

    public static void shutdown() {
        if (SESSION_FACTORY.isOpen()) SESSION_FACTORY.close();
    }
}