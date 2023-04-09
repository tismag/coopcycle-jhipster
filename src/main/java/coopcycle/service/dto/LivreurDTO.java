package coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link coopcycle.domain.Livreur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LivreurDTO implements Serializable {

    private Long id;

    private Long idLivreur;

    private String nom;

    private String prenom;

    private String email;

    private String numeroTel;

    private String cooperative;

    private CooperativeDTO cooperative;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdLivreur() {
        return idLivreur;
    }

    public void setIdLivreur(Long idLivreur) {
        this.idLivreur = idLivreur;
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

    public String getCooperative() {
        return cooperative;
    }

    public void setCooperative(String cooperative) {
        this.cooperative = cooperative;
    }

    public CooperativeDTO getCooperative() {
        return cooperative;
    }

    public void setCooperative(CooperativeDTO cooperative) {
        this.cooperative = cooperative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LivreurDTO)) {
            return false;
        }

        LivreurDTO livreurDTO = (LivreurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, livreurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivreurDTO{" +
            "id=" + getId() +
            ", idLivreur=" + getIdLivreur() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", numeroTel='" + getNumeroTel() + "'" +
            ", cooperative='" + getCooperative() + "'" +
            ", cooperative=" + getCooperative() +
            "}";
    }
}
