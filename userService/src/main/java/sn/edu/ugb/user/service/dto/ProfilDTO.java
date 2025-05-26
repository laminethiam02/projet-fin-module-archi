package sn.edu.ugb.user.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.user.domain.Profil} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfilDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomComplet;

    private String adresse;

    @NotNull
    private String email;

    private String telephone;

    @NotNull
    private Long compteId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getCompteId() {
        return compteId;
    }

    public void setCompteId(Long compteId) {
        this.compteId = compteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfilDTO)) {
            return false;
        }

        ProfilDTO profilDTO = (ProfilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfilDTO{" +
            "id=" + getId() +
            ", nomComplet='" + getNomComplet() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", compteId=" + getCompteId() +
            "}";
    }
}
