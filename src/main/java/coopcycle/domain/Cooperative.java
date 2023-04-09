package coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Cooperative.
 */
@Table("cooperative")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cooperative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("id_cooperative")
    private Long idCooperative;

    @Column("nom")
    private String nom;

    @Column("email")
    private String email;

    @Column("numero_tel")
    private String numeroTel;

    @Column("adresse")
    private String adresse;

    @Transient
    @JsonIgnoreProperties(value = { "idCommercants" }, allowSetters = true)
    private Set<Commercant> idCooperatives = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cooperative id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCooperative() {
        return this.idCooperative;
    }

    public Cooperative idCooperative(Long idCooperative) {
        this.setIdCooperative(idCooperative);
        return this;
    }

    public void setIdCooperative(Long idCooperative) {
        this.idCooperative = idCooperative;
    }

    public String getNom() {
        return this.nom;
    }

    public Cooperative nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return this.email;
    }

    public Cooperative email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroTel() {
        return this.numeroTel;
    }

    public Cooperative numeroTel(String numeroTel) {
        this.setNumeroTel(numeroTel);
        return this;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Cooperative adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Set<Commercant> getIdCooperatives() {
        return this.idCooperatives;
    }

    public void setIdCooperatives(Set<Commercant> commercants) {
        if (this.idCooperatives != null) {
            this.idCooperatives.forEach(i -> i.removeIdCommercant(this));
        }
        if (commercants != null) {
            commercants.forEach(i -> i.addIdCommercant(this));
        }
        this.idCooperatives = commercants;
    }

    public Cooperative idCooperatives(Set<Commercant> commercants) {
        this.setIdCooperatives(commercants);
        return this;
    }

    public Cooperative addIdCooperative(Commercant commercant) {
        this.idCooperatives.add(commercant);
        commercant.getIdCommercants().add(this);
        return this;
    }

    public Cooperative removeIdCooperative(Commercant commercant) {
        this.idCooperatives.remove(commercant);
        commercant.getIdCommercants().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cooperative)) {
            return false;
        }
        return id != null && id.equals(((Cooperative) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cooperative{" +
            "id=" + getId() +
            ", idCooperative=" + getIdCooperative() +
            ", nom='" + getNom() + "'" +
            ", email='" + getEmail() + "'" +
            ", numeroTel='" + getNumeroTel() + "'" +
            ", adresse='" + getAdresse() + "'" +
            "}";
    }
}
