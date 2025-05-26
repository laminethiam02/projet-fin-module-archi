package sn.edu.ugb.grade.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.grade.domain.Bareme} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BaremeDTO implements Serializable {

    private Long id;

    private String intitule;

    @NotNull
    private Integer totalPoints;

    @NotNull
    private Long examenId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Long getExamenId() {
        return examenId;
    }

    public void setExamenId(Long examenId) {
        this.examenId = examenId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaremeDTO)) {
            return false;
        }

        BaremeDTO baremeDTO = (BaremeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, baremeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BaremeDTO{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", totalPoints=" + getTotalPoints() +
            ", examenId=" + getExamenId() +
            "}";
    }
}
