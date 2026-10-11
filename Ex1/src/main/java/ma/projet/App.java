package ma.projet;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class App {
    public static void main(String[] args) {
        CategorieService categories = new CategorieService();
        ProduitService produits = new ProduitService();
        CommandeService commandes = new CommandeService();
        LigneCommandeService lignes = new LigneCommandeService();

        try {
            Categorie reseau = categories.ajouter(new Categorie("NET", "Equipement reseau"));
            Categorie stockage = categories.ajouter(new Categorie("STO", "Stockage numerique"));

            Produit routeur = produits.ajouter(new Produit("NX-740", new BigDecimal("129.90"), reseau));
            Produit adaptateur = produits.ajouter(new Produit("AX-215", new BigDecimal("88.50"), reseau));
            Produit disque = produits.ajouter(new Produit("NV-620", new BigDecimal("209.00"), stockage));
            Produit memoire = produits.ajouter(new Produit("RM-316", new BigDecimal("154.75"), stockage));

            Commande commande = commandes.ajouter(new Commande(LocalDate.of(2026, 3, 14)));
            Commande commandeAncienne = commandes.ajouter(new Commande(LocalDate.of(2026, 2, 20)));
            commande.ajouterLigne(routeur, 3);
            commande.ajouterLigne(adaptateur, 6);
            commande.ajouterLigne(disque, 2);
            commandes.modifier(commande);
            commandeAncienne.ajouterLigne(memoire, 4);
            commandes.modifier(commandeAncienne);

            DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
            System.out.println("--- Produits de la categorie reseau ---");
            produits.listerParCategorie(reseau.getId()).forEach(p -> System.out.println(p.getReference() + " : " + p.getPrix() + " DH"));

            System.out.println("\n--- Produits commandes entre le 1 et le 31 mars 2026 ---");
            produits.listerCommandesEntre(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31))
                    .forEach(p -> System.out.println(p.getReference()));
            String mois = commande.getDate().format(DateTimeFormatter.ofPattern("MMMM", Locale.FRENCH));
            String dateAffichee = commande.getDate().getDayOfMonth() + " "
                    + Character.toUpperCase(mois.charAt(0)) + mois.substring(1) + " " + commande.getDate().getYear();
            System.out.println("Commande : " + commande.getId() + "     Date : " + dateAffichee);
            System.out.println("Liste des produits :");
            System.out.println("R\u00e9f\u00e9rence   Prix       Quantit\u00e9");
            lignes.listerPourCommande(commande.getId()).forEach(ligne ->
                    System.out.printf("%-12s %-8s DH %d%n", ligne.getProduit().getReference(),
                            ligne.getProduit().getPrix().setScale(2).toPlainString(), ligne.getQuantite()));

            System.out.println("\n--- Produits a plus de 100 DH (requete nommee) ---");
            produits.listerPrixSuperieurA100Dh().forEach(p -> System.out.println(p.getReference() + " : " + p.getPrix() + " DH"));

            List<Commande> commandesMars = commandes.listerEntre(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31));
            System.out.println("\nCommandes du mois : " + commandesMars.size());
        } finally {
            HibernateUtil.shutdown();
        }
    }
}