package ma.projet.service;

import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public abstract class ServiceGenerique<T, ID extends Serializable> implements IDao<T, ID> {
    private final Class<T> typeEntite;

    protected ServiceGenerique(Class<T> typeEntite) { this.typeEntite = typeEntite; }

    @Override
    public T ajouter(T entite) {
        return ecrire(session -> { session.persist(entite); return entite; });
    }

    @Override
    public T modifier(T entite) { return ecrire(session -> (T) session.merge(entite)); }

    @Override
    public void supprimer(T entite) {
        ecrire(session -> {
            T attachee = session.contains(entite) ? entite : (T) session.merge(entite);
            session.remove(attachee);
            return null;
        });
    }

    @Override
    public void supprimerParId(ID id) {
        ecrire(session -> {
            T trouvée = session.get(typeEntite, id);
            if (trouvée != null) session.remove(trouvée);
            return null;
        });
    }

    @Override
    public T chercher(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(typeEntite, id);
        }
    }

    @Override
    public List<T> lister() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM " + typeEntite.getSimpleName(), typeEntite).list();
        }
    }

    private <R> R ecrire(Function<Session, R> operation) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                R resultat = operation.apply(session);
                transaction.commit();
                return resultat;
            } catch (RuntimeException erreur) {
                if (transaction.isActive()) transaction.rollback();
                throw erreur;
            }
        }
    }
}