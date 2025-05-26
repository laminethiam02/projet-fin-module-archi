package sn.edu.ugb.student.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.student.domain.ReleveNote} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReleveNoteDTO implements Serializable {

    private Long id;

    @NotNull
    private String semestre;

    @NotNull
    private Float noteGlobale;

    @NotNull
    private Long dossierId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public Float getNoteGlobale() {
        return noteGlobale;
    }

    public void setNoteGlobale(Float noteGlobale) {
        this.noteGlobale = noteGlobale;
    }

    public Long getDossierId() {
        return dossierId;
    }

    public void setDossierId(Long dossierId) {
        this.dossierId = dossierId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReleveNoteDTO)) {
            return false;
        }

        ReleveNoteDTO releveNoteDTO = (ReleveNoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, releveNoteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReleveNoteDTO{" +
            "id=" + getId() +
            ", semestre='" + getSemestre() + "'" +
            ", noteGlobale=" + getNoteGlobale() +
            ", dossierId=" + getDossierId() +
            "}";
    }
}
