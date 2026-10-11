package ma.projet;

import ma.projet.beans.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class App {
    private static Date date(int annee, int mois, int jour) {
        Calendar c=Calendar.getInstance(); c.clear(); c.set(annee, mois-1, jour); return c.getTime();
    }
    public static void main(String[] args) {
        try { System.setOut(new PrintStream(System.out, true, "UTF-8")); }
        catch (java.io.UnsupportedEncodingException erreur) { throw new IllegalStateException(erreur); }
        // Ce profil permet d'essayer l'application sans serveur MySQL local.
        if (args.length > 0 && "--h2-demo".equals(args[0])) System.setProperty("app.profile", "h2-demo");
        HommeService hommes=new HommeService(); FemmeService femmes=new FemmeService(); MariageService mariages=new MariageService();
        try {
            reinitialiserDonneesExemple();
            Homme said=hommes.creer(new Homme("SAFI", "NABIL", "0611203040", "Rabat", date(1972, 6, 8)));
            Homme karim=hommes.creer(new Homme("BENALI", "KARIM", "0611203041", "Sale", date(1980, 2, 14)));
            Homme youssef=hommes.creer(new Homme("EL FASSI", "YOUSSEF", "0611203042", "Temara", date(1976, 9, 21)));
            Homme hamza=hommes.creer(new Homme("IDRISSI", "HAMZA", "0611203043", "Kenitra", date(1985, 1, 2)));
            Homme amine=hommes.creer(new Homme("LAHLOU", "AMINE", "0611203044", "Rabat", date(1968, 11, 19)));
            Femme[] liste={
                new Femme("RAMI","SALMA","0622000001","Rabat",date(1976,4,18)),
                new Femme("ALI","NOURA","0622000002","Sale",date(1981,8,9)),
                new Femme("ALAOUI","IMANE","0622000003","Temara",date(1984,12,12)),
                new Femme("BENJELLOUN","HIBA","0622000004","Rabat",date(1978,3,22)),
                new Femme("TAZI","MERYEM","0622000005","Kenitra",date(1990,7,5)),
                new Femme("CHERKAOUI","LINA","0622000006","Sale",date(1992,5,30)),
                new Femme("BERRADA","SARA","0622000007","Rabat",date(1970,2,11)),
                new Femme("MANSOURI","AYA","0622000008","Temara",date(1988,10,1)),
                new Femme("EL KHATIB","DOHA","0622000009","Kenitra",date(1965,1,16)),
                new Femme("AMRANI","YASMINE","0622000010","Rabat",date(1995,6,6))
            };
            for (int i=0;i<liste.length;i++) liste[i]=femmes.creer(liste[i]);
            // Les unions passées et actuelles servent à illustrer les requêtes historiques.
            mariages.creer(new Mariage(said,liste[0],date(1991,3,2),date(1997,10,15),0));
            mariages.creer(new Mariage(said,liste[0],date(1998,5,12),null,3));
            mariages.creer(new Mariage(said,liste[1],date(2004,9,3),null,2));
            mariages.creer(new Mariage(said,liste[2],date(2009,4,18),null,1));
            mariages.creer(new Mariage(said,liste[3],date(2014,11,7),null,2));
            mariages.creer(new Mariage(karim,liste[4],date(2007,2,2),date(2012,6,30),1));
            mariages.creer(new Mariage(karim,liste[5],date(2016,1,15),null,0));
            mariages.creer(new Mariage(youssef,liste[6],date(2001,3,1),null,2));
            mariages.creer(new Mariage(hamza,liste[7],date(2019,8,20),null,1));
            mariages.creer(new Mariage(amine,liste[8],date(1990,6,4),date(2000,6,4),2));
            mariages.creer(new Mariage(amine,liste[9],date(2003,7,9),null,3));

            System.out.println("--- Liste des femmes ---");
            femmes.listerFemmes().forEach(f -> System.out.println("- " + f.getNomComplet()));
            Femme plusAgee=femmes.trouverLaPlusAgee();
            System.out.println("\nFemme la plus âgée : " + plusAgee.getNomComplet());
            SimpleDateFormat formatDate=new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("\nÉpouses de " + said.getNomComplet() + " (1990–2020) :");
            hommes.listerEpousesEntre(said.getId(),date(1990,1,1),date(2020,12,31))
                    .forEach(m -> System.out.println("- " + m.getFemme().getNomComplet() + " depuis le " + formatDate.format(m.getDateDebut())));
            System.out.println("Enfants de " + liste[0].getNomComplet() + " entre 1995 et 2010 : "
                    + femmes.compterEnfantsEntre(liste[0].getId(),date(1995,1,1),date(2010,12,31)));
            System.out.println("\nFemmes mariées au moins deux fois :");
            femmes.listerMarieesAuMoinsDeuxFois().forEach(f -> System.out.println("- " + f.getNomComplet()));
            System.out.println("Hommes mariés à quatre femmes entre 1990 et 2020 : "
                    + mariages.compterHommesAvecQuatreEpouses(date(1990,1,1),date(2020,12,31)));
            System.out.println("\nDétails des mariages de " + said.getNomComplet() + " :");
            afficherMariages(mariages.listerParHomme(said.getId()),formatDate);
        } finally { HibernateUtil.fermer(); }
    }
    private static void reinitialiserDonneesExemple() {
        List<String> telephonesHommes=Arrays.asList("0611203040","0611203041","0611203042","0611203043","0611203044");
        List<String> telephonesFemmes=Arrays.asList("0622000001","0622000002","0622000003","0622000004","0622000005",
                "0622000006","0622000007","0622000008","0622000009","0622000010");
        try (Session session=HibernateUtil.getSessionFactory().openSession()) {
            List<Homme> hommesExistants=session.createQuery("from Homme h where h.telephone in :telephones",Homme.class)
                    .setParameterList("telephones",telephonesHommes).getResultList();
            List<Femme> femmesExistantes=session.createQuery("from Femme f where f.telephone in :telephones",Femme.class)
                    .setParameterList("telephones",telephonesFemmes).getResultList();
            if (hommesExistants.isEmpty() && femmesExistantes.isEmpty()) return;
            List<Long> idsHommes=new ArrayList<>();
            for (Homme h:hommesExistants) idsHommes.add(h.getId());
            List<Long> idsFemmes=new ArrayList<>();
            for (Femme f:femmesExistantes) idsFemmes.add(f.getId());
            if (idsHommes.isEmpty()) idsHommes.add(-1L);
            if (idsFemmes.isEmpty()) idsFemmes.add(-1L);
            Transaction tx=session.beginTransaction();
            try {
                session.createQuery("delete from Mariage m where m.homme.id in :hommes or m.femme.id in :femmes")
                        .setParameterList("hommes",idsHommes).setParameterList("femmes",idsFemmes).executeUpdate();
                for (Homme h:hommesExistants) session.remove(h);
                for (Femme f:femmesExistantes) session.remove(f);
                tx.commit();
            } catch (RuntimeException erreur) {
                if (tx.isActive()) tx.rollback();
                throw erreur;
            }
        }
    }
    private static void afficherMariages(List<Mariage> liste, SimpleDateFormat formatDate) {
        System.out.println("Mariages en cours :");
        int numero=1;
        for (Mariage m:liste) if (m.estEnCours()) {
            System.out.printf("%d. Femme : %-24s Date début : %-12s Nbr enfants : %d%n", numero++,m.getFemme().getNomComplet(),formatDate.format(m.getDateDebut()),m.getNbrEnfant());
        }
        System.out.println("\nMariages échoués :");
        numero=1;
        for (Mariage m:liste) if (!m.estEnCours()) {
            System.out.printf("%d. Femme : %-24s Date début : %-12s Date fin : %-12s Nbr enfants : %d%n", numero++,m.getFemme().getNomComplet(),formatDate.format(m.getDateDebut()),formatDate.format(m.getDateFin()),m.getNbrEnfant());
        }
    }
}