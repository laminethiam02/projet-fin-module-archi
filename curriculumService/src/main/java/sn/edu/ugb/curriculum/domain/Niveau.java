package sn.edu.ugb.curriculum.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Niveau.
 */
@Entity
@Table(name = "niveau")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Niveau implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "ordre", nullable = false)
    private Integer ordre;

    @NotNull
    @Column(name = "programme_id", nullable = false)
    private Long programmeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Niveau id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Niveau libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getOrdre() {
        return this.ordre;
    }

    public Niveau ordre(Integer ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Long getProgrammeId() {
        return this.programmeId;
    }

    public Niveau programmeId(Long programmeId) {
        this.setProgrammeId(programmeId);
        return this;
    }

    public void setProgrammeId(Long programmeId) {
        this.programmeId = programmeId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Niveau)) {
            return false;
        }
        return getId() != null && getId().equals(((Niveau) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Niveau{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", ordre=" + getOrdre() +
            ", programmeId=" + getProgrammeId() +
            "}";
    }
}
