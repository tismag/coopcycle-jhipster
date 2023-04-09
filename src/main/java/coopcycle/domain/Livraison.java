package coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Livraison.
 */
@Table("livraison")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Livraison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("id_livraison")
    private Long idLivraison;

    @Column("contenu")
    private String contenu;

    @Column("quantite")
    private Long quantite;

    @Transient
    @JsonIgnoreProperties(value = { "idLivreurs", "cooperative" }, allowSetters = true)
    private Livreur livreur;

    @Column("livreur_id")
    private Long livreurId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Livraison id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdLivraison() {
        return this.idLivraison;
    }

    public Livraison idLivraison(Long idLivraison) {
        this.setIdLivraison(idLivraison);
        return this;
    }

    public void setIdLivraison(Long idLivraison) {
        this.idLivraison = idLivraison;
    }

    public String getContenu() {
        return this.contenu;
    }

    public Livraison contenu(String contenu) {
        this.setContenu(contenu);
        return this;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Long getQuantite() {
        return this.quantite;
    }

    public Livraison quantite(Long quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public Livreur getLivreur() {
        return this.livreur;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
        this.livreurId = livreur != null ? livreur.getId() : null;
    }

    public Livraison livreur(Livreur livreur) {
        this.setLivreur(livreur);
        return this;
    }

    public Long getLivreurId() {
        return this.livreurId;
    }

    public void setLivreurId(Long livreur) {
        this.livreurId = livreur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livraison)) {
            return false;
        }
        return id != null && id.equals(((Livraison) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Livraison{" +
            "id=" + getId() +
            ", idLivraison=" + getIdLivraison() +
            ", contenu='" + getContenu() + "'" +
            ", quantite=" + getQuantite() +
            "}";
    }
}
