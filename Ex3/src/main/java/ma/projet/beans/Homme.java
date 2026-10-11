package ma.projet.beans;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "hommes")
public class Homme extends Personne {
    public Homme() { }
    public Homme(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }
}