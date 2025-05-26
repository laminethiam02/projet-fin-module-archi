package sn.edu.ugb.curriculum.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.curriculum.domain.Niveau} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NiveauDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    @NotNull
    private Integer ordre;

    @NotNull
    private Long programmeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Long getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(Long programmeId) {
        this.programmeId = programmeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NiveauDTO)) {
            return false;
        }

        NiveauDTO niveauDTO = (NiveauDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, niveauDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NiveauDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", ordre=" + getOrdre() +
            ", programmeId=" + getProgrammeId() +
            "}";
    }
}
