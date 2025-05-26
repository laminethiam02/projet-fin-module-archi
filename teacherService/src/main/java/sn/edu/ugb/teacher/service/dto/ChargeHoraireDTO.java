package sn.edu.ugb.teacher.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.teacher.domain.ChargeHoraire} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChargeHoraireDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer nombreHeures;

    @NotNull
    private Long contratId;

    @NotNull
    private Long classeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNombreHeures() {
        return nombreHeures;
    }

    public void setNombreHeures(Integer nombreHeures) {
        this.nombreHeures = nombreHeures;
    }

    public Long getContratId() {
        return contratId;
    }

    public void setContratId(Long contratId) {
        this.contratId = contratId;
    }

    public Long getClasseId() {
        return classeId;
    }

    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChargeHoraireDTO)) {
            return false;
        }

        ChargeHoraireDTO chargeHoraireDTO = (ChargeHoraireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chargeHoraireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChargeHoraireDTO{" +
            "id=" + getId() +
            ", nombreHeures=" + getNombreHeures() +
            ", contratId=" + getContratId() +
            ", classeId=" + getClasseId() +
            "}";
    }
}
