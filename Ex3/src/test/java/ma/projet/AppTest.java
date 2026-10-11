package ma.projet;

import ma.projet.beans.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Calendar;
import java.util.Date;
import static org.junit.Assert.*;

public class AppTest {
    private static HommeService hommes;
    private static FemmeService femmes;
    private static MariageService mariages;
    private static Homme homme;
    private static Femme femmeA;
    private static Femme femmeB;
    private static Femme femmeC;
    private static Femme femmeD;
    private static Femme femmeRepete;

    private static Date date(int annee,int mois,int jour) { Calendar c=Calendar.getInstance(); c.clear(); c.set(annee,mois-1,jour); return c.getTime(); }
    @BeforeClass public static void preparer() {
        System.setProperty("app.profile","h2-demo");
        hommes=new HommeService(); femmes=new FemmeService(); mariages=new MariageService();
        homme=hommes.creer(new Homme("TEST","ADIL","0600000001","Rabat",date(1970,1,1)));
        Homme h2=hommes.creer(new Homme("TEST","OMAR","0600000002","Sale",date(1972,2,2)));
        Homme h3=hommes.creer(new Homme("TEST","AMINE","0600000003","Rabat",date(1973,3,3)));
        Homme h4=hommes.creer(new Homme("TEST","KARIM","0600000004","Sale",date(1974,4,4)));
        Homme h5=hommes.creer(new Homme("TEST","SAID","0600000005","Rabat",date(1975,5,5)));
        Femme[] f=new Femme[10];
        for(int i=0;i<f.length;i++) f[i]=femmes.creer(new Femme("FEMME"+i,"TEST","06000001"+i,"Rabat",date(1990+i,1,1)));
        femmeA=f[0]; femmeB=f[1]; femmeC=f[2]; femmeD=f[3]; femmeRepete=f[4];
        mariages.creer(new Mariage(homme,f[0],date(2000,1,1),null,2));
        mariages.creer(new Mariage(homme,f[1],date(2001,1,1),null,1));
        mariages.creer(new Mariage(homme,f[2],date(2002,1,1),null,0));
        mariages.creer(new Mariage(homme,f[3],date(2003,1,1),null,3));
        mariages.creer(new Mariage(h2,f[4],date(2000,1,1),date(2005,1,1),1));
        mariages.creer(new Mariage(h3,f[4],date(2006,1,1),null,2));
        mariages.creer(new Mariage(h4,f[5],date(2000,1,1),null,0));
        mariages.creer(new Mariage(h5,f[6],date(2000,1,1),null,1));
    }
    @AfterClass public static void terminer() { HibernateUtil.fermer(); System.clearProperty("app.profile"); }

    @Test public void testCreationDeCinqHommesEtDixFemmes() { assertEquals(5,hommes.listerTout().size()); assertEquals(10,femmes.listerFemmes().size()); }
    @Test public void testFemmeLaPlusAgee() { assertEquals("FEMME0",femmes.trouverLaPlusAgee().getNom()); }
    @Test public void testEpousesEntreDates() { assertEquals(4,hommes.listerEpousesEntre(homme.getId(),date(1999,1,1),date(2004,12,31)).size()); }
    @Test public void testNombreEnfantsEntreDates() { assertEquals(2,femmes.compterEnfantsEntre(femmeA.getId(),date(1999,1,1),date(2000,12,31))); }
    @Test public void testFemmesMarieesAuMoinsDeuxFois() { assertTrue(femmes.listerMarieesAuMoinsDeuxFois().stream().anyMatch(f -> f.getId().equals(femmeRepete.getId()))); }
    @Test public void testHommesAvecQuatreFemmesParCriteria() { assertEquals(1,mariages.compterHommesAvecQuatreEpouses(date(1999,1,1),date(2004,12,31))); }
    @Test public void testDetailsDesMariages() { assertEquals(4,mariages.listerParHomme(homme.getId()).size()); }
}