package com.demo;

import com.demo.classes.*;
import com.demo.service.*;
import com.demo.util.HibernateUtil;
import java.text.SimpleDateFormat;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class App {
    private static Date date(int annee, int mois, int jour) {
        Calendar calendrier = Calendar.getInstance();
        calendrier.clear();
        calendrier.set(annee, mois - 1, jour);
        return calendrier.getTime();
    }

    public static void main(String[] args) {
        try { System.setOut(new PrintStream(System.out, true, "UTF-8")); }
        catch (java.io.UnsupportedEncodingException impossible) { throw new IllegalStateException(impossible); }
        EmployeService employes = new EmployeService();
        ProjetService projets = new ProjetService();
        TacheService taches = new TacheService();
        EmployeTacheService affectations = new EmployeTacheService();
        try {
            Employe pilote = employes.creer(new Employe("El Amrani", "Salma", "0612345678"));
            Employe ingenieur = employes.creer(new Employe("Bennani", "Yassine", "0623456789"));
            Projet projet = projets.creer(new Projet("Plateforme logistique", date(2026, 1, 14), date(2026, 7, 30), pilote));

            Tache cadrage = new Tache("Etude des besoins", date(2026, 2, 10), date(2026, 2, 20), 850.0);
            cadrage.setProjet(projet);
            Tache architecture = new Tache("Architecture technique", date(2026, 3, 10), date(2026, 3, 15), 1450.0);
            architecture.setProjet(projet);
            Tache developpement = new Tache("Module inventaire", date(2026, 4, 10), date(2026, 4, 25), 3200.0);
            developpement.setProjet(projet);
            Tache recette = new Tache("Recette fonctionnelle", date(2026, 5, 4), date(2026, 5, 12), 1100.0);
            recette.setProjet(projet);
            taches.creer(cadrage);
            taches.creer(architecture);
            taches.creer(developpement);
            taches.creer(recette);

            affectations.creer(new EmployeTache(ingenieur, cadrage, date(2026, 2, 11), date(2026, 2, 21)));
            affectations.creer(new EmployeTache(pilote, architecture, date(2026, 3, 11), date(2026, 3, 16)));
            affectations.creer(new EmployeTache(ingenieur, developpement, date(2026, 4, 11), date(2026, 4, 26)));

            SimpleDateFormat dateLongue = new SimpleDateFormat("d MMMM yyyy", Locale.FRENCH);
            String dateProjet = dateLongue.format(projet.getDateDebut());
            int debutMois = dateProjet.indexOf(" ") + 1;
            dateProjet = dateProjet.substring(0, debutMois) + Character.toUpperCase(dateProjet.charAt(debutMois)) + dateProjet.substring(debutMois + 1);
            System.out.println("Projet : " + projet.getId() + "      Nom : " + projet.getNom()
                    + "     Date début : " + dateProjet);
            System.out.println("Liste des tâches planifiées :");
            System.out.printf("%-5s %-25s %-16s %-16s%n", "Num", "Nom", "Date début", "Date fin");
            for (Tache t : projets.listerTachesPlanifiees(projet.getId())) {
                System.out.printf("%-5d %-25s %-16s %-16s%n", t.getId(), t.getNom(),
                        new SimpleDateFormat("dd/MM/yyyy").format(t.getDateDebut()),
                        new SimpleDateFormat("dd/MM/yyyy").format(t.getDateFin()));
            }

            System.out.println("\nTâches réalisées avec dates réelles :");
            System.out.printf("%-5s %-25s %-20s %-20s%n", "Num", "Nom", "Début réel", "Fin réelle");
            for (EmployeTache et : projets.listerTachesRealisees(projet.getId())) {
                System.out.printf("%-5d %-25s %-20s %-20s%n", et.getTache().getId(), et.getTache().getNom(),
                        new SimpleDateFormat("dd/MM/yyyy").format(et.getDateDebutReelle()),
                        new SimpleDateFormat("dd/MM/yyyy").format(et.getDateFinReelle()));
            }

            System.out.println("\nProjets gérés par " + pilote.getPrenom() + " " + pilote.getNom() + " : ");
            employes.listerProjetsGeres(pilote.getId()).forEach(p -> System.out.println("- " + p.getNom()));
            System.out.println("Tâches réalisées par " + ingenieur.getPrenom() + " " + ingenieur.getNom() + " :");
            employes.listerTachesRealisees(ingenieur.getId()).forEach(et -> System.out.println("- " + et.getTache().getNom()));

            System.out.println("\nTâches à plus de 1000 DH (requête nommée) :");
            taches.listerPrixSuperieurA1000().forEach(t -> System.out.printf("- %s : %.2f DH%n", t.getNom(), t.getPrix()));
            System.out.println("Tâches réalisées entre le 01/03/2026 et le 30/04/2026 :");
            List<EmployeTache> faitesMarsAvril = taches.listerRealiseesEntre(date(2026, 3, 1), date(2026, 4, 30));
            faitesMarsAvril.forEach(et -> System.out.println("- " + et.getTache().getNom()));
        } finally {
            HibernateUtil.fermer();
        }
    }
}