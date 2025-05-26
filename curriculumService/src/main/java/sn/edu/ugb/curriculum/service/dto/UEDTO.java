package sn.edu.ugb.curriculum.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.curriculum.domain.UE} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UEDTO implements Serializable {

    private Long id;

    @NotNull
    private String intitule;

    @NotNull
    private Integer credits;

    @NotNull
    private Long niveauId;

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

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Long getNiveauId() {
        return niveauId;
    }

    public void setNiveauId(Long niveauId) {
        this.niveauId = niveauId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UEDTO)) {
            return false;
        }

        UEDTO uEDTO = (UEDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uEDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UEDTO{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", credits=" + getCredits() +
            ", niveauId=" + getNiveauId() +
            "}";
    }
}
