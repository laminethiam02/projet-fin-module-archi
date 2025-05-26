package sn.edu.ugb.student.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.student.domain.DossierEtudiant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DossierEtudiantDTO implements Serializable {

    private Long id;

    @NotNull
    private String matricule;

    private String nationalite;

    private LocalDate dateNaissance;

    @NotNull
    private Long profilId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Long getProfilId() {
        return profilId;
    }

    public void setProfilId(Long profilId) {
        this.profilId = profilId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DossierEtudiantDTO)) {
            return false;
        }

        DossierEtudiantDTO dossierEtudiantDTO = (DossierEtudiantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dossierEtudiantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DossierEtudiantDTO{" +
            "id=" + getId() +
            ", matricule='" + getMatricule() + "'" +
            ", nationalite='" + getNationalite() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", profilId=" + getProfilId() +
            "}";
    }
}
