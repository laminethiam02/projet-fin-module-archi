package sn.edu.ugb.curriculum.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.curriculum.domain.MatiereProgramme} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MatiereProgrammeDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomMatiere;

    private Integer heuresCours;

    @NotNull
    private Long ueId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomMatiere() {
        return nomMatiere;
    }

    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    public Integer getHeuresCours() {
        return heuresCours;
    }

    public void setHeuresCours(Integer heuresCours) {
        this.heuresCours = heuresCours;
    }

    public Long getUeId() {
        return ueId;
    }

    public void setUeId(Long ueId) {
        this.ueId = ueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatiereProgrammeDTO)) {
            return false;
        }

        MatiereProgrammeDTO matiereProgrammeDTO = (MatiereProgrammeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, matiereProgrammeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatiereProgrammeDTO{" +
            "id=" + getId() +
            ", nomMatiere='" + getNomMatiere() + "'" +
            ", heuresCours=" + getHeuresCours() +
            ", ueId=" + getUeId() +
            "}";
    }
}
