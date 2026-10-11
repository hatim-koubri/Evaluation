package ma.projet.classes;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate date;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneCommandeProduit> lignes = new ArrayList<>();

    public Commande() { }
    public Commande(LocalDate date) { this.date = date; }

    public void ajouterLigne(Produit produit, int quantite) {
        lignes.add(new LigneCommandeProduit(this, produit, quantite));
    }

    public Integer getId() { return id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public List<LigneCommandeProduit> getLignes() { return lignes; }

    @Override
    public String toString() { return "Commande{id=" + id + ", date=" + date + "}"; }
}