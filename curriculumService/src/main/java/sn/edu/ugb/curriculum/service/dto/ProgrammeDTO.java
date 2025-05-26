package sn.edu.ugb.curriculum.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.curriculum.domain.Programme} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProgrammeDTO implements Serializable {

    private Long id;

    @NotNull
    private String intitule;

    @NotNull
    private String codeProgramme;

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

    public String getCodeProgramme() {
        return codeProgramme;
    }

    public void setCodeProgramme(String codeProgramme) {
        this.codeProgramme = codeProgramme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgrammeDTO)) {
            return false;
        }

        ProgrammeDTO programmeDTO = (ProgrammeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, programmeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgrammeDTO{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", codeProgramme='" + getCodeProgramme() + "'" +
            "}";
    }
}
