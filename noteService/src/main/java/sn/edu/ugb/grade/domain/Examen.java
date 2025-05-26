package sn.edu.ugb.grade.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Examen.
 */
@Entity
@Table(name = "examen")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Examen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "type_examen", nullable = false)
    private String typeExamen;

    @NotNull
    @Column(name = "date_examen", nullable = false)
    private LocalDate dateExamen;

    @NotNull
    @Column(name = "matiere_id", nullable = false)
    private Long matiereId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Examen id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeExamen() {
        return this.typeExamen;
    }

    public Examen typeExamen(String typeExamen) {
        this.setTypeExamen(typeExamen);
        return this;
    }

    public void setTypeExamen(String typeExamen) {
        this.typeExamen = typeExamen;
    }

    public LocalDate getDateExamen() {
        return this.dateExamen;
    }

    public Examen dateExamen(LocalDate dateExamen) {
        this.setDateExamen(dateExamen);
        return this;
    }

    public void setDateExamen(LocalDate dateExamen) {
        this.dateExamen = dateExamen;
    }

    public Long getMatiereId() {
        return this.matiereId;
    }

    public Examen matiereId(Long matiereId) {
        this.setMatiereId(matiereId);
        return this;
    }

    public void setMatiereId(Long matiereId) {
        this.matiereId = matiereId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Examen)) {
            return false;
        }
        return getId() != null && getId().equals(((Examen) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Examen{" +
            "id=" + getId() +
            ", typeExamen='" + getTypeExamen() + "'" +
            ", dateExamen='" + getDateExamen() + "'" +
            ", matiereId=" + getMatiereId() +
            "}";
    }
}
