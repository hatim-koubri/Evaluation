package com.projet;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeId;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class AppTest {
    private final CategorieService categories = new CategorieService();
    private final ProduitService produits = new ProduitService();
    private final CommandeService commandes = new CommandeService();
    private final LigneCommandeService lignes = new LigneCommandeService();

    @Before
    public void nettoyerBase() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM LigneCommandeProduit").executeUpdate();
            session.createQuery("DELETE FROM Commande").executeUpdate();
            session.createQuery("DELETE FROM Produit").executeUpdate();
            session.createQuery("DELETE FROM Categorie").executeUpdate();
            tx.commit();
        }
    }

    @Test
    public void daoEtRechercheParCategorieEtPrix() {
        Categorie famille = categories.ajouter(new Categorie("PER", "Peripheriques"));
        Produit souris = produits.ajouter(new Produit("MS-204", new BigDecimal("79.90"), famille));
        Produit ecran = produits.ajouter(new Produit("MN-880", new BigDecimal("249.00"), famille));

        assertNotNull(souris.getId());
        assertEquals(2, produits.listerParCategorie(famille.getId()).size());
        assertTrue(produits.listerPrixSuperieurA100Dh().stream().anyMatch(p -> p.getId().equals(ecran.getId())));
        assertFalse(produits.listerPrixSuperieurA100Dh().stream().anyMatch(p -> p.getId().equals(souris.getId())));

        ecran.setPrix(new BigDecimal("269.00"));
        produits.modifier(ecran);
        assertEquals(0, new BigDecimal("269.00").compareTo(produits.chercher(ecran.getId()).getPrix()));
        produits.supprimerParId(souris.getId());
        assertNull(produits.chercher(souris.getId()));
    }

    @Test
    public void filtreLesProduitsSelonLaDateEtLaCommande() {
        Categorie famille = categories.ajouter(new Categorie("COM", "Composants"));
        Produit processeur = produits.ajouter(new Produit("CP-610", new BigDecimal("520.00"), famille));
        Produit carte = produits.ajouter(new Produit("MB-325", new BigDecimal("310.00"), famille));
        Commande mars = commandes.ajouter(new Commande(LocalDate.of(2026, 3, 14)));
        Commande fevrier = commandes.ajouter(new Commande(LocalDate.of(2026, 2, 20)));

        mars.ajouterLigne(processeur, 2);
        mars.ajouterLigne(carte, 1);
        commandes.modifier(mars);
        fevrier.ajouterLigne(processeur, 1);
        commandes.modifier(fevrier);

        List<Produit> marsProduits = produits.listerCommandesEntre(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31));
        assertEquals(2, marsProduits.size());
        List<LigneCommandeProduit> lignesMars = produits.listerDansCommande(mars.getId());
        assertEquals(2, lignesMars.size());
        assertEquals(1, commandes.listerEntre(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31)).size());
        assertTrue(lignesMars.stream().anyMatch(l -> l.getId().equals(new LigneCommandeId(mars.getId(), processeur.getId()))));
        assertEquals(1, lignes.listerPourCommande(fevrier.getId()).size());
    }

    @AfterClass
    public static void fermerHibernate() {
        HibernateUtil.shutdown();
    }
}