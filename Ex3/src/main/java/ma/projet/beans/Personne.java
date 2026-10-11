package ma.projet.beans;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "personnes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Personne {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 80) private String nom;
    @Column(nullable = false, length = 80) private String prenom;
    private String telephone;
    private String adresse;
    @Temporal(TemporalType.DATE) @Column(name = "date_naissance") private Date dateNaissance;

    protected Personne() { }
    protected Personne(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        this.nom=nom; this.prenom=prenom; this.telephone=telephone; this.adresse=adresse; this.dateNaissance=dateNaissance;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getNomComplet() { return nom + " " + prenom; }
}