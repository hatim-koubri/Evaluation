package ma.projet.service;

import ma.projet.classes.LigneCommandeProduit;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class LigneCommandeService extends ServiceGenerique<LigneCommandeProduit, ma.projet.classes.LigneCommandeId> {
    public LigneCommandeService() { super(LigneCommandeProduit.class); }

    public List<LigneCommandeProduit> listerPourCommande(Integer commandeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT ligne FROM LigneCommandeProduit ligne " +
                            "JOIN FETCH ligne.produit WHERE ligne.commande.id = :numeroCommande " +
                            "ORDER BY ligne.produit.reference", LigneCommandeProduit.class)
                    .setParameter("numeroCommande", commandeId)
                    .list();
        }
    }
}