package com.demo.classes;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employe_tache")
public class EmployeTache {
    @EmbeddedId private EmployeTacheId id = new EmployeTacheId();
    @ManyToOne(fetch = FetchType.LAZY) @MapsId("employeId")
    @JoinColumn(name = "employe_id") private Employe employe;
    @ManyToOne(fetch = FetchType.LAZY) @MapsId("tacheId")
    @JoinColumn(name = "tache_id") private Tache tache;
    @Temporal(TemporalType.DATE) private Date dateDebutReelle;
    @Temporal(TemporalType.DATE) private Date dateFinReelle;

    public EmployeTache() { }
    public EmployeTache(Employe employe, Tache tache, Date dateDebutReelle, Date dateFinReelle) {
        this.employe = employe; this.tache = tache;
        this.id = new EmployeTacheId(employe.getId(), tache.getId());
        this.dateDebutReelle = dateDebutReelle; this.dateFinReelle = dateFinReelle;
    }
    public EmployeTacheId getId() { return id; }
    public void setId(EmployeTacheId id) { this.id = id; }
    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }
    public Tache getTache() { return tache; }
    public void setTache(Tache tache) { this.tache = tache; }
    public Date getDateDebutReelle() { return dateDebutReelle; }
    public void setDateDebutReelle(Date dateDebutReelle) { this.dateDebutReelle = dateDebutReelle; }
    public Date getDateFinReelle() { return dateFinReelle; }
    public void setDateFinReelle(Date dateFinReelle) { this.dateFinReelle = dateFinReelle; }
}