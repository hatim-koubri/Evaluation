package com.demo.service;

import com.demo.classes.EmployeTache;
import com.demo.classes.Projet;
import com.demo.classes.Tache;
import com.demo.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class ProjetService extends ServiceGenerique<Projet, Integer> {
    public ProjetService() { super(Projet.class); }
    public List<Tache> listerTachesPlanifiees(int projetId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from Tache t where t.projet.id = :id order by t.dateDebut", Tache.class)
                    .setParameter("id", projetId).getResultList();
        }
    }
    public List<EmployeTache> listerTachesRealisees(int projetId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("select et from EmployeTache et join fetch et.tache join fetch et.employe where et.tache.projet.id = :id order by et.dateDebutReelle", EmployeTache.class)
                    .setParameter("id", projetId).getResultList();
        }
    }
}