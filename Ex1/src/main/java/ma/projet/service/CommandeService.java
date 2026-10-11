package ma.projet.service;

import ma.projet.classes.Commande;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class CommandeService extends ServiceGenerique<Commande, Integer> {
    public CommandeService() { super(Commande.class); }

    public List<Commande> listerEntre(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null || debut.isAfter(fin)) {
            throw new IllegalArgumentException("La periode de commande est invalide.");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Commande c WHERE c.date BETWEEN :debutPeriode AND :finPeriode ORDER BY c.date",
                            Commande.class)
                    .setParameter("debutPeriode", debut)
                    .setParameter("finPeriode", fin)
                    .list();
        }
    }
}