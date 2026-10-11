package com.demo.classes;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projets")
public class Projet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
    @Column(nullable = false, length = 120) private String nom;
    @Temporal(TemporalType.DATE) private Date dateDebut;
    @Temporal(TemporalType.DATE) private Date dateFin;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "chef_projet_id", nullable = false) private Employe chefProjet;
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true) private List<Tache> taches = new ArrayList<>();
    public Projet() { }
    public Projet(String nom, Date dateDebut, Date dateFin) { this.nom=nom; this.dateDebut=dateDebut; this.dateFin=dateFin; }
    public Projet(String nom, Date dateDebut, Date dateFin, Employe chefProjet) { this(nom,dateDebut,dateFin); this.chefProjet=chefProjet; }
    public void ajouterTache(Tache tache) { taches.add(tache); tache.setProjet(this); }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public Employe getChefProjet() { return chefProjet; }
    public void setChefProjet(Employe chefProjet) { this.chefProjet = chefProjet; }
    public List<Tache> getTaches() { return taches; }
    public void setTaches(List<Tache> taches) { this.taches = taches; }
}