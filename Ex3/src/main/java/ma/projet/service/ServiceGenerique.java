package ma.projet.service;

import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.io.Serializable;
import java.util.List;

public class ServiceGenerique<T, ID extends Serializable> implements IDao<T, ID> {
    private final Class<T> type;
    protected ServiceGenerique(Class<T> type) { this.type=type; }
    @Override public T creer(T objet) { return ecriture(s -> { s.persist(objet); return objet; }); }
    @Override public T modifier(T objet) { return ecriture(s -> (T) s.merge(objet)); }
    @Override public void supprimer(T objet) { ecriture(s -> { s.remove(s.contains(objet) ? objet : s.merge(objet)); return null; }); }
    @Override public T chercherParId(ID id) { try (Session s=HibernateUtil.getSessionFactory().openSession()) { return s.get(type,id); } }
    @Override public List<T> listerTout() { try (Session s=HibernateUtil.getSessionFactory().openSession()) { return s.createQuery("from " + type.getSimpleName(), type).getResultList(); } }
    protected <R> R ecriture(Operation<R> operation) {
        try (Session s=HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx=s.beginTransaction();
            try { R resultat=operation.executer(s); tx.commit(); return resultat; }
            catch (RuntimeException erreur) { if (tx.isActive()) tx.rollback(); throw erreur; }
        }
    }
    @FunctionalInterface protected interface Operation<R> { R executer(Session session); }
}