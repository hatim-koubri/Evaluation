package com.demo;

import com.demo.classes.*;
import com.demo.service.*;
import com.demo.util.HibernateUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Calendar;
import java.util.Date;
import static org.junit.Assert.*;

public class AppTest {
    private static EmployeService employes;
    private static ProjetService projets;
    private static TacheService taches;
    private static EmployeTacheService affectations;
    private static Employe pilote;
    private static Employe ingenieur;
    private static Projet projet;
    private static Tache conception;

    private static Date date(int annee, int mois, int jour) {
        Calendar c = Calendar.getInstance(); c.clear(); c.set(annee, mois - 1, jour); return c.getTime();
    }

    @BeforeClass public static void initialiserDonnees() {
        employes = new EmployeService(); projets = new ProjetService(); taches = new TacheService(); affectations = new EmployeTacheService();
        pilote = employes.creer(new Employe("Test-Pilote", "Lina", "0600000011"));
        ingenieur = employes.creer(new Employe("Test-Ingenieur", "Omar", "0600000022"));
        projet = projets.creer(new Projet("Projet test Atlas", date(2026, 1, 5), date(2026, 8, 1), pilote));
        Tache analyse = new Tache("Analyse test", date(2026, 2, 1), date(2026, 2, 10), 750);
        analyse.setProjet(projet); taches.creer(analyse);
        conception = new Tache("Conception test", date(2026, 3, 1), date(2026, 3, 12), 1800);
        conception.setProjet(projet); taches.creer(conception);
        affectations.creer(new EmployeTache(ingenieur, conception, date(2026, 3, 2), date(2026, 3, 14)));
    }

    @AfterClass public static void fermer() { HibernateUtil.fermer(); }

    @Test public void testProjetsGeresEtTachesEmploye() {
        assertEquals(1, employes.listerProjetsGeres(pilote.getId()).size());
        assertEquals(1, employes.listerTachesRealisees(ingenieur.getId()).size());
    }
    @Test public void testTachesPlanifieesEtRealiseesProjet() {
        assertEquals(2, projets.listerTachesPlanifiees(projet.getId()).size());
        assertEquals(1, projets.listerTachesRealisees(projet.getId()).size());
        assertEquals("Conception test", projets.listerTachesRealisees(projet.getId()).get(0).getTache().getNom());
    }
    @Test public void testRequeteNommeePrixSuperieurA1000() {
        assertTrue(taches.listerPrixSuperieurA1000().stream().anyMatch(t -> t.getId() == conception.getId()));
    }
    @Test public void testTachesRealiseesEntreDeuxDates() {
        assertEquals(1, taches.listerRealiseesEntre(date(2026, 3, 1), date(2026, 3, 31)).size());
        assertTrue(taches.listerRealiseesEntre(date(2026, 5, 1), date(2026, 5, 31)).isEmpty());
    }
}