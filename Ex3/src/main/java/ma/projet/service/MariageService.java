package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import java.util.Date;
import java.util.List;

public class MariageService extends ServiceGenerique<Mariage, Long> {
    public MariageService() { super(Mariage.class); }
    @Override public Mariage creer(Mariage mariage) {
        return ecriture(s -> {
            Homme homme=s.get(Homme.class, mariage.getHomme().getId());
            Femme femme=s.get(Femme.class, mariage.getFemme().getId());
            if (homme == null || femme == null) throw new IllegalArgumentException("Homme ou femme introuvable");
            mariage.setHomme(homme); mariage.setFemme(femme); s.persist(mariage); return mariage;
        });
    }
    public List<Mariage> listerParHomme(Long hommeId) {
        try (Session s=HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("select m from Mariage m join fetch m.femme where m.homme.id = :id order by m.dateDebut", Mariage.class)
                    .setParameter("id", hommeId).getResultList();
        }
    }
    public long compterHommesAvecQuatreEpouses(Date debut, Date fin) {
        HommeService.verifierIntervalle(debut, fin);
        try (Session s=HibernateUtil.getSessionFactory().openSession()) {
            javax.persistence.criteria.CriteriaBuilder cb=s.getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery<Long> cq=cb.createQuery(Long.class);
            javax.persistence.criteria.Root<Homme> homme=cq.from(Homme.class);
            javax.persistence.criteria.Subquery<Long> sq=cq.subquery(Long.class);
            javax.persistence.criteria.Root<Mariage> mariage=sq.from(Mariage.class);
            sq.select(cb.countDistinct(mariage.get("femme").get("id")));
            sq.where(cb.equal(mariage.get("homme").get("id"), homme.get("id")),
                    cb.lessThanOrEqualTo(mariage.<Date>get("dateDebut"), fin),
                    cb.or(cb.isNull(mariage.get("dateFin")), cb.greaterThanOrEqualTo(mariage.<Date>get("dateFin"), debut)));
            cq.select(cb.count(homme)).where(cb.ge(sq, 4L));
            return s.createQuery(cq).getSingleResult();
        }
    }
}