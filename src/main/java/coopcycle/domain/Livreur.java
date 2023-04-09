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
 * A Livreur.
 */
@Table("livreur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Livreur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("id_livreur")
    private Long idLivreur;

    @Column("nom")
    private String nom;

    @Column("prenom")
    private String prenom;

    @Column("email")
    private String email;

    @Column("numero_tel")
    private String numeroTel;

    @Column("cooperative")
    private String cooperative;

    @Transient
    @JsonIgnoreProperties(value = { "livreur" }, allowSetters = true)
    private Set<Livraison> idLivreurs = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "idCooperatives" }, allowSetters = true)
    private Cooperative cooperative;

    @Column("cooperative_id")
    private Long cooperativeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Livreur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdLivreur() {
        return this.idLivreur;
    }

    public Livreur idLivreur(Long idLivreur) {
        this.setIdLivreur(idLivreur);
        return this;
    }

    public void setIdLivreur(Long idLivreur) {
        this.idLivreur = idLivreur;
    }

    public String getNom() {
        return this.nom;
    }

    public Livreur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Livreur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public Livreur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroTel() {
        return this.numeroTel;
    }

    public Livreur numeroTel(String numeroTel) {
        this.setNumeroTel(numeroTel);
        return this;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getCooperative() {
        return this.cooperative;
    }

    public Livreur cooperative(String cooperative) {
        this.setCooperative(cooperative);
        return this;
    }

    public void setCooperative(String cooperative) {
        this.cooperative = cooperative;
    }

    public Set<Livraison> getIdLivreurs() {
        return this.idLivreurs;
    }

    public void setIdLivreurs(Set<Livraison> livraisons) {
        if (this.idLivreurs != null) {
            this.idLivreurs.forEach(i -> i.setLivreur(null));
        }
        if (livraisons != null) {
            livraisons.forEach(i -> i.setLivreur(this));
        }
        this.idLivreurs = livraisons;
    }

    public Livreur idLivreurs(Set<Livraison> livraisons) {
        this.setIdLivreurs(livraisons);
        return this;
    }

    public Livreur addIdLivreur(Livraison livraison) {
        this.idLivreurs.add(livraison);
        livraison.setLivreur(this);
        return this;
    }

    public Livreur removeIdLivreur(Livraison livraison) {
        this.idLivreurs.remove(livraison);
        livraison.setLivreur(null);
        return this;
    }

    public Cooperative getCooperative() {
        return this.cooperative;
    }

    public void setCooperative(Cooperative cooperative) {
        this.cooperative = cooperative;
        this.cooperativeId = cooperative != null ? cooperative.getId() : null;
    }

    public Livreur cooperative(Cooperative cooperative) {
        this.setCooperative(cooperative);
        return this;
    }

    public Long getCooperativeId() {
        return this.cooperativeId;
    }

    public void setCooperativeId(Long cooperative) {
        this.cooperativeId = cooperative;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livreur)) {
            return false;
        }
        return id != null && id.equals(((Livreur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Livreur{" +
            "id=" + getId() +
            ", idLivreur=" + getIdLivreur() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", numeroTel='" + getNumeroTel() + "'" +
            ", cooperative='" + getCooperative() + "'" +
            "}";
    }
}
