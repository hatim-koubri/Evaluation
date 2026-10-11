package ma.projet.service;

import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ProduitService extends ServiceGenerique<Produit, Integer> {
    public ProduitService() { super(Produit.class); }

    public List<Produit> listerParCategorie(Integer categorieId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Produit p WHERE p.categorie.id = :idCategorie ORDER BY p.reference",
                            Produit.class)
                    .setParameter("idCategorie", categorieId)
                    .list();
        }
    }

    public List<Produit> listerCommandesEntre(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null || debut.isAfter(fin)) {
            throw new IllegalArgumentException("La periode de recherche est invalide.");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT DISTINCT p FROM Produit p " +
                            "JOIN p.lignesCommande ligne JOIN ligne.commande commande " +
                            "WHERE commande.date BETWEEN :dateDebut AND :dateFin ORDER BY p.reference",
                            Produit.class)
                    .setParameter("dateDebut", debut)
                    .setParameter("dateFin", fin)
                    .list();
        }
    }

    public List<LigneCommandeProduit> listerDansCommande(Integer commandeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT ligne FROM LigneCommandeProduit ligne " +
                            "JOIN FETCH ligne.produit WHERE ligne.commande.id = :numeroCommande " +
                            "ORDER BY ligne.produit.reference", LigneCommandeProduit.class)
                    .setParameter("numeroCommande", commandeId)
                    .list();
        }
    }

    public List<Produit> listerPrixSuperieurA100Dh() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Produit.prixAuDessusDuSeuil", Produit.class)
                    .setParameter("seuilPrix", new BigDecimal("100.00"))
                    .list();
        }
    }
}