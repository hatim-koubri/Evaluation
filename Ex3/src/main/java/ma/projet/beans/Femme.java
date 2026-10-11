package ma.projet.beans;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "femmes")
@NamedQueries({
    @NamedQuery(name = "Femme.marieesAuMoinsDeuxFois",
            query = "select f from Femme f where (select count(m) from Mariage m where m.femme = f) >= 2 order by f.nom, f.prenom")
})
@NamedNativeQueries({
    @NamedNativeQuery(name = "Femme.nombreEnfantsEntreDates",
            query = "select coalesce(sum(nbr_enfant), 0) from mariages where femme_id = :femmeId and date_debut <= :dateFin and (date_fin is null or date_fin >= :dateDebut)")
})
public class Femme extends Personne {
    public Femme() { }
    public Femme(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }
}