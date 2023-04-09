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
 * A Commercant.
 */
@Table("commercant")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Commercant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("id_commercant")
    private Long idCommercant;

    @Column("nom")
    private String nom;

    @Column("prenom")
    private String prenom;

    @Column("email")
    private String email;

    @Column("numero_tel")
    private String numeroTel;

    @Column("nom_etablissement")
    private String nomEtablissement;

    @Column("adresse")
    private String adresse;

    @Transient
    @JsonIgnoreProperties(value = { "idCooperatives" }, allowSetters = true)
    private Set<Cooperative> idCommercants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commercant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCommercant() {
        return this.idCommercant;
    }

    public Commercant idCommercant(Long idCommercant) {
        this.setIdCommercant(idCommercant);
        return this;
    }

    public void setIdCommercant(Long idCommercant) {
        this.idCommercant = idCommercant;
    }

    public String getNom() {
        return this.nom;
    }

    public Commercant nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Commercant prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public Commercant email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroTel() {
        return this.numeroTel;
    }

    public Commercant numeroTel(String numeroTel) {
        this.setNumeroTel(numeroTel);
        return this;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getNomEtablissement() {
        return this.nomEtablissement;
    }

    public Commercant nomEtablissement(String nomEtablissement) {
        this.setNomEtablissement(nomEtablissement);
        return this;
    }

    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Commercant adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Set<Cooperative> getIdCommercants() {
        return this.idCommercants;
    }

    public void setIdCommercants(Set<Cooperative> cooperatives) {
        this.idCommercants = cooperatives;
    }

    public Commercant idCommercants(Set<Cooperative> cooperatives) {
        this.setIdCommercants(cooperatives);
        return this;
    }

    public Commercant addIdCommercant(Cooperative cooperative) {
        this.idCommercants.add(cooperative);
        cooperative.getIdCooperatives().add(this);
        return this;
    }

    public Commercant removeIdCommercant(Cooperative cooperative) {
        this.idCommercants.remove(cooperative);
        cooperative.getIdCooperatives().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commercant)) {
            return false;
        }
        return id != null && id.equals(((Commercant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commercant{" +
            "id=" + getId() +
            ", idCommercant=" + getIdCommercant() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", numeroTel='" + getNumeroTel() + "'" +
            ", nomEtablissement='" + getNomEtablissement() + "'" +
            ", adresse='" + getAdresse() + "'" +
            "}";
    }
}
