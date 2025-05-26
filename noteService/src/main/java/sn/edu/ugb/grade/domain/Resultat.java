package sn.edu.ugb.grade.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Resultat.
 */
@Entity
@Table(name = "resultat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Resultat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "note_obtenue", nullable = false)
    private Float noteObtenue;

    @Column(name = "commentaire")
    private String commentaire;

    @NotNull
    @Column(name = "examen_id", nullable = false)
    private Long examenId;

    @NotNull
    @Column(name = "dossier_id", nullable = false)
    private Long dossierId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Resultat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getNoteObtenue() {
        return this.noteObtenue;
    }

    public Resultat noteObtenue(Float noteObtenue) {
        this.setNoteObtenue(noteObtenue);
        return this;
    }

    public void setNoteObtenue(Float noteObtenue) {
        this.noteObtenue = noteObtenue;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Resultat commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Long getExamenId() {
        return this.examenId;
    }

    public Resultat examenId(Long examenId) {
        this.setExamenId(examenId);
        return this;
    }

    public void setExamenId(Long examenId) {
        this.examenId = examenId;
    }

    public Long getDossierId() {
        return this.dossierId;
    }

    public Resultat dossierId(Long dossierId) {
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
        if (!(o instanceof Resultat)) {
            return false;
        }
        return getId() != null && getId().equals(((Resultat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resultat{" +
            "id=" + getId() +
            ", noteObtenue=" + getNoteObtenue() +
            ", commentaire='" + getCommentaire() + "'" +
            ", examenId=" + getExamenId() +
            ", dossierId=" + getDossierId() +
            "}";
    }
}
