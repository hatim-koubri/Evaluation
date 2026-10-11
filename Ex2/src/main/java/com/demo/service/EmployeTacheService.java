package com.demo.service;

import com.demo.classes.Employe;
import com.demo.classes.EmployeTache;
import com.demo.classes.EmployeTacheId;
import com.demo.classes.Tache;
import org.hibernate.Session;

public class EmployeTacheService extends ServiceGenerique<EmployeTache, EmployeTacheId> {
    public EmployeTacheService() { super(EmployeTache.class); }

    @Override public EmployeTache creer(EmployeTache affectation) {
        return executerEcriture(session -> {
            Employe employeLie = session.get(Employe.class, affectation.getEmploye().getId());
            Tache tacheLiee = session.get(Tache.class, affectation.getTache().getId());
            if (employeLie == null || tacheLiee == null) throw new IllegalArgumentException("Employé ou tâche inexistant");
            affectation.setEmploye(employeLie);
            affectation.setTache(tacheLiee);
            affectation.setId(new EmployeTacheId(employeLie.getId(), tacheLiee.getId()));
            session.persist(affectation);
            return affectation;
        });
    }
}