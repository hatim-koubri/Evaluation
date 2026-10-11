package com.demo.service;

import com.demo.dao.IDao;
import com.demo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.io.Serializable;
import java.util.List;

public class ServiceGenerique<T, ID extends Serializable> implements IDao<T, ID> {
    private final Class<T> typeEntite;

    public ServiceGenerique(Class<T> typeEntite) { this.typeEntite = typeEntite; }

    @Override public T creer(T entite) { return executerEcriture(session -> { session.persist(entite); return entite; }); }
    @Override public T modifier(T entite) { return executerEcriture(session -> (T) session.merge(entite)); }
    @Override public void supprimer(T entite) { executerEcriture(session -> { session.remove(session.contains(entite) ? entite : session.merge(entite)); return null; }); }
    @Override public T chercherParId(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) { return session.get(typeEntite, id); }
    }
    @Override public List<T> listerTout() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from " + typeEntite.getSimpleName(), typeEntite).getResultList();
        }
    }

    protected <R> R executerEcriture(Operation<R> operation) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                R resultat = operation.executer(session);
                transaction.commit();
                return resultat;
            } catch (RuntimeException erreur) {
                if (transaction.isActive()) transaction.rollback();
                throw erreur;
            }
        }
    }
    @FunctionalInterface protected interface Operation<R> { R executer(Session session); }
}