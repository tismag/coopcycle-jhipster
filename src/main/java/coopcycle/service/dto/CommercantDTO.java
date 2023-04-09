package coopcycle.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link coopcycle.domain.Commercant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommercantDTO implements Serializable {

    private Long id;

    private Long idCommercant;

    private String nom;

    private String prenom;

    private String email;

    private String numeroTel;

    private String nomEtablissement;

    private String adresse;

    private Set<CooperativeDTO> idCommercants = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCommercant() {
        return idCommercant;
    }

    public void setIdCommercant(Long idCommercant) {
        this.idCommercant = idCommercant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getNomEtablissement() {
        return nomEtablissement;
    }

    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Set<CooperativeDTO> getIdCommercants() {
        return idCommercants;
    }

    public void setIdCommercants(Set<CooperativeDTO> idCommercants) {
        this.idCommercants = idCommercants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommercantDTO)) {
            return false;
        }

        CommercantDTO commercantDTO = (CommercantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commercantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommercantDTO{" +
            "id=" + getId() +
            ", idCommercant=" + getIdCommercant() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", numeroTel='" + getNumeroTel() + "'" +
            ", nomEtablissement='" + getNomEtablissement() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", idCommercants=" + getIdCommercants() +
            "}";
    }
}
