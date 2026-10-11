package ma.projet.classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produits")
@NamedQuery(name = "Produit.prixAuDessusDuSeuil",
        query = "SELECT p FROM Produit p WHERE p.prix > :seuilPrix ORDER BY p.reference")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 40)
    private String reference;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @OneToMany(mappedBy = "produit", cascade = javax.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<LigneCommandeProduit> lignesCommande = new ArrayList<>();

    public Produit() { }
    public Produit(String reference, BigDecimal prix, Categorie categorie) {
        this.reference = reference;
        this.prix = prix;
        this.categorie = categorie;
    }

    public Integer getId() { return id; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public List<LigneCommandeProduit> getLignesCommande() { return lignesCommande; }

    @Override
    public String toString() { return "Produit{ref='" + reference + "', prix=" + prix + " DH}"; }
}