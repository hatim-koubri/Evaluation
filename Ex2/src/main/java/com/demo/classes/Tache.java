package com.demo.classes;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "taches")
@NamedQuery(name = "Tache.prixSuperieurA1000", query = "select t from Tache t where t.prix > :prixMinimum order by t.prix desc")
public class Tache {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
    @Column(nullable = false, length = 120) private String nom;
    @Temporal(TemporalType.DATE) private Date dateDebut;
    @Temporal(TemporalType.DATE) private Date dateFin;
    private double prix;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "projet_id", nullable = false) private Projet projet;
    @OneToMany(mappedBy = "tache", cascade = CascadeType.ALL, orphanRemoval = true) private List<EmployeTache> affectations = new ArrayList<>();
    public Tache() { }
    public Tache(String nom, Date dateDebut, Date dateFin, double prix) { this.nom=nom; this.dateDebut=dateDebut; this.dateFin=dateFin; this.prix=prix; }
    public Tache(int id, String nom, Date dateDebut, Date dateFin, double prix) { this(nom,dateDebut,dateFin,prix); this.id=id; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public Projet getProjet() { return projet; }
    public void setProjet(Projet projet) { this.projet = projet; }
    public List<EmployeTache> getAffectations() { return affectations; }
    public void setAffectations(List<EmployeTache> affectations) { this.affectations = affectations; }
}