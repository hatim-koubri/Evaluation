package com.demo.service;

import com.demo.classes.Employe;
import com.demo.classes.EmployeTache;
import com.demo.classes.Projet;
import com.demo.classes.Tache;
import org.hibernate.Session;
import com.demo.util.HibernateUtil;
import java.util.List;

public class EmployeService extends ServiceGenerique<Employe, Integer> {
    public EmployeService() { super(Employe.class); }
    public List<EmployeTache> listerTachesRealisees(int employeId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("select et from EmployeTache et join fetch et.tache where et.employe.id = :id order by et.dateDebutReelle", EmployeTache.class)
                    .setParameter("id", employeId).getResultList();
        }
    }
    public List<Projet> listerProjetsGeres(int employeId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from Projet p where p.chefProjet.id = :id order by p.dateDebut", Projet.class)
                    .setParameter("id", employeId).getResultList();
        }
    }
}