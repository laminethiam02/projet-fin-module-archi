package sn.edu.ugb.grade.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.grade.domain.Resultat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResultatDTO implements Serializable {

    private Long id;

    @NotNull
    private Float noteObtenue;

    private String commentaire;

    @NotNull
    private Long examenId;

    @NotNull
    private Long dossierId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getNoteObtenue() {
        return noteObtenue;
    }

    public void setNoteObtenue(Float noteObtenue) {
        this.noteObtenue = noteObtenue;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Long getExamenId() {
        return examenId;
    }

    public void setExamenId(Long examenId) {
        this.examenId = examenId;
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
        if (!(o instanceof ResultatDTO)) {
            return false;
        }

        ResultatDTO resultatDTO = (ResultatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resultatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResultatDTO{" +
            "id=" + getId() +
            ", noteObtenue=" + getNoteObtenue() +
            ", commentaire='" + getCommentaire() + "'" +
            ", examenId=" + getExamenId() +
            ", dossierId=" + getDossierId() +
            "}";
    }
}
