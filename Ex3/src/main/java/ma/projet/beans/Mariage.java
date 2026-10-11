package ma.projet.beans;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mariages")
public class Mariage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "homme_id", nullable = false)
    private Homme homme;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "femme_id", nullable = false)
    private Femme femme;
    @Temporal(TemporalType.DATE) @Column(name = "date_debut", nullable = false)
    private Date dateDebut;
    @Temporal(TemporalType.DATE) @Column(name = "date_fin")
    private Date dateFin;
    @Column(name = "nbr_enfant", nullable = false)
    private int nbrEnfant;

    public Mariage() { }
    public Mariage(Homme homme, Femme femme, Date dateDebut, Date dateFin, int nbrEnfant) {
        this.homme=homme; this.femme=femme; this.dateDebut=dateDebut; this.dateFin=dateFin; this.nbrEnfant=nbrEnfant;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id=id; }
    public Homme getHomme() { return homme; }
    public void setHomme(Homme homme) { this.homme=homme; }
    public Femme getFemme() { return femme; }
    public void setFemme(Femme femme) { this.femme=femme; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut=dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin=dateFin; }
    public int getNbrEnfant() { return nbrEnfant; }
    public void setNbrEnfant(int nbrEnfant) { this.nbrEnfant=nbrEnfant; }
    public boolean estEnCours() { return dateFin == null; }
}