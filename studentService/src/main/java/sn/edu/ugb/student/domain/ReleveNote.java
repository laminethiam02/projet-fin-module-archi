package sn.edu.ugb.student.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReleveNote.
 */
@Entity
@Table(name = "releve_note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReleveNote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "semestre", nullable = false)
    private String semestre;

    @NotNull
    @Column(name = "note_globale", nullable = false)
    private Float noteGlobale;

    @NotNull
    @Column(name = "dossier_id", nullable = false)
    private Long dossierId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReleveNote id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSemestre() {
        return this.semestre;
    }

    public ReleveNote semestre(String semestre) {
        this.setSemestre(semestre);
        return this;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public Float getNoteGlobale() {
        return this.noteGlobale;
    }

    public ReleveNote noteGlobale(Float noteGlobale) {
        this.setNoteGlobale(noteGlobale);
        return this;
    }

    public void setNoteGlobale(Float noteGlobale) {
        this.noteGlobale = noteGlobale;
    }

    public Long getDossierId() {
        return this.dossierId;
    }

    public ReleveNote dossierId(Long dossierId) {
        this.setDossierId(dossierId);
        return this;
    }

    public void setDossierId(Long dossierId) {
        this.dossierId = dossierId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReleveNote)) {
            return false;
        }
        return getId() != null && getId().equals(((ReleveNote) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReleveNote{" +
            "id=" + getId() +
            ", semestre='" + getSemestre() + "'" +
            ", noteGlobale=" + getNoteGlobale() +
            ", dossierId=" + getDossierId() +
            "}";
    }
}
