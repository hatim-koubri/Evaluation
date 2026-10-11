package ma.projet.dao;

import java.io.Serializable;
import java.util.List;

public interface IDao<T, ID extends Serializable> {
    T creer(T entite);
    T modifier(T entite);
    void supprimer(T entite);
    T chercherParId(ID id);
    List<T> listerTout();
}