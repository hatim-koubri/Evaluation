package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import java.util.Date;
import java.util.List;

public class FemmeService extends ServiceGenerique<Femme, Long> {
    public FemmeService() { super(Femme.class); }
    public List<Femme> listerFemmes() { return listerTout(); }
    public Femme trouverLaPlusAgee() {
        try (Session s=HibernateUtil.getSessionFactory().openSession()) {
            List<Femme> femmes=s.createQuery("from Femme f order by f.dateNaissance asc", Femme.class).setMaxResults(1).getResultList();
            return femmes.isEmpty() ? null : femmes.get(0);
        }
    }
    public long compterEnfantsEntre(Long femmeId, Date debut, Date fin) {
        HommeService.verifierIntervalle(debut, fin);
        try (Session s=HibernateUtil.getSessionFactory().openSession()) {
            Number total=(Number)s.createNamedQuery("Femme.nombreEnfantsEntreDates")
                    .setParameter("femmeId", femmeId).setParameter("dateDebut", debut).setParameter("dateFin", fin).getSingleResult();
            return total.longValue();
        }
    }
    public List<Femme> listerMarieesAuMoinsDeuxFois() {
        try (Session s=HibernateUtil.getSessionFactory().openSession()) {
            return s.createNamedQuery("Femme.marieesAuMoinsDeuxFois", Femme.class).getResultList();
        }
    }
}