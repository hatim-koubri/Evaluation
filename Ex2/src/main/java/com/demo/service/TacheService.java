package com.demo.service;

import com.demo.classes.EmployeTache;
import com.demo.classes.Tache;
import com.demo.util.HibernateUtil;
import org.hibernate.Session;
import java.util.Date;
import java.util.List;

public class TacheService extends ServiceGenerique<Tache, Integer> {
    public TacheService() { super(Tache.class); }
    public List<Tache> listerPrixSuperieurA1000() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createNamedQuery("Tache.prixSuperieurA1000", Tache.class)
                    .setParameter("prixMinimum", 1000.0).getResultList();
        }
    }
    public List<EmployeTache> listerRealiseesEntre(Date debut, Date fin) {
        if (debut == null || fin == null || debut.after(fin)) throw new IllegalArgumentException("Intervalle de dates invalide");
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("select et from EmployeTache et join fetch et.tache where et.dateDebutReelle >= :debut and et.dateFinReelle <= :fin order by et.dateDebutReelle", EmployeTache.class)
                    .setParameter("debut", debut).setParameter("fin", fin).getResultList();
        }
    }
}