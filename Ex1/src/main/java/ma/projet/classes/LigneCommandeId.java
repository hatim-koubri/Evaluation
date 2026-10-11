package ma.projet.classes;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LigneCommandeId implements Serializable {
    @Column(name = "commande_id")
    private Integer commandeId;

    @Column(name = "produit_id")
    private Integer produitId;

    public LigneCommandeId() { }
    public LigneCommandeId(Integer commandeId, Integer produitId) {
        this.commandeId = commandeId;
        this.produitId = produitId;
    }
    public Integer getCommandeId() { return commandeId; }
    public void setCommandeId(Integer commandeId) { this.commandeId = commandeId; }
    public Integer getProduitId() { return produitId; }
    public void setProduitId(Integer produitId) { this.produitId = produitId; }

    @Override
    public boolean equals(Object autre) {
        if (this == autre) return true;
        if (!(autre instanceof LigneCommandeId)) return false;
        LigneCommandeId cle = (LigneCommandeId) autre;
        return Objects.equals(commandeId, cle.commandeId) && Objects.equals(produitId, cle.produitId);
    }
    @Override
    public int hashCode() { return Objects.hash(commandeId, produitId); }
}