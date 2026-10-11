package ma.projet.classes;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 100)
    private String libelle;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produit> produits = new ArrayList<>();

    public Categorie() { }
    public Categorie(String code, String libelle) { this.code = code; this.libelle = libelle; }

    // Ce helper met a jour les deux extremites de la relation.
    public void ajouterProduit(Produit produit) {
        produits.add(produit);
        produit.setCategorie(this);
    }

    public void retirerProduit(Produit produit) {
        produits.remove(produit);
        produit.setCategorie(null);
    }

    public Integer getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public List<Produit> getProduits() { return produits; }

    @Override
    public String toString() { return "Categorie{id=" + id + ", code='" + code + "', libelle='" + libelle + "'}"; }
}