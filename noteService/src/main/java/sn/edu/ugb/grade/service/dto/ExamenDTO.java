package sn.edu.ugb.grade.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.grade.domain.Examen} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamenDTO implements Serializable {

    private Long id;

    @NotNull
    private String typeExamen;

    @NotNull
    private LocalDate dateExamen;

    @NotNull
    private Long matiereId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeExamen() {
        return typeExamen;
    }

    public void setTypeExamen(String typeExamen) {
        this.typeExamen = typeExamen;
    }

    public LocalDate getDateExamen() {
        return dateExamen;
    }

    public void setDateExamen(LocalDate dateExamen) {
        this.dateExamen = dateExamen;
    }

    public Long getMatiereId() {
        return matiereId;
    }

    public void setMatiereId(Long matiereId) {
        this.matiereId = matiereId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamenDTO)) {
            return false;
        }

        ExamenDTO examenDTO = (ExamenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamenDTO{" +
            "id=" + getId() +
            ", typeExamen='" + getTypeExamen() + "'" +
            ", dateExamen='" + getDateExamen() + "'" +
            ", matiereId=" + getMatiereId() +
            "}";
    }
}
