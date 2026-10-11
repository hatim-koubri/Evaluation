package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import java.util.Date;
import java.util.List;

public class HommeService extends ServiceGenerique<Homme, Long> {
    public HommeService() { super(Homme.class); }
    public List<Mariage> listerEpousesEntre(Long hommeId, Date debut, Date fin) {
        verifierIntervalle(debut, fin);
        try (Session s=HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("select m from Mariage m join fetch m.femme where m.homme.id = :id and m.dateDebut <= :fin and (m.dateFin is null or m.dateFin >= :debut) order by m.dateDebut", Mariage.class)
                    .setParameter("id", hommeId).setParameter("debut", debut).setParameter("fin", fin).getResultList();
        }
    }
    static void verifierIntervalle(Date debut, Date fin) {
        if (debut == null || fin == null || debut.after(fin)) throw new IllegalArgumentException("Intervalle de dates invalide");
    }
}