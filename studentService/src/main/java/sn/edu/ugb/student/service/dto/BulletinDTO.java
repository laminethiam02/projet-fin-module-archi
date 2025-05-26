package sn.edu.ugb.student.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.student.domain.Bulletin} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BulletinDTO implements Serializable {

    private Long id;

    @NotNull
    private String anneeAcademique;

    private Float moyenne;

    private String mention;

    @NotNull
    private Long dossierId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(String anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public Float getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(Float moyenne) {
        this.moyenne = moyenne;
    }

    public String getMention() {
        return mention;
    }

    public void setMention(String mention) {
        this.mention = mention;
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
        if (!(o instanceof BulletinDTO)) {
            return false;
        }

        BulletinDTO bulletinDTO = (BulletinDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bulletinDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BulletinDTO{" +
            "id=" + getId() +
            ", anneeAcademique='" + getAnneeAcademique() + "'" +
            ", moyenne=" + getMoyenne() +
            ", mention='" + getMention() + "'" +
            ", dossierId=" + getDossierId() +
            "}";
    }
}
