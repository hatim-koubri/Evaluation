package ma.projet.dao;

import java.io.Serializable;
import java.util.List;

public interface IDao<T, ID extends Serializable> {
    T ajouter(T entite);
    T modifier(T entite);
    void supprimer(T entite);
    void supprimerParId(ID id);
    T chercher(ID id);
    List<T> lister();
}