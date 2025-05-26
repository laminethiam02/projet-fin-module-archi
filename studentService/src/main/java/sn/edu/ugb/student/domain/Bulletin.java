package sn.edu.ugb.student.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bulletin.
 */
@Entity
@Table(name = "bulletin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bulletin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "annee_academique", nullable = false)
    private String anneeAcademique;

    @Column(name = "moyenne")
    private Float moyenne;

    @Column(name = "mention")
    private String mention;

    @NotNull
    @Column(name = "dossier_id", nullable = false)
    private Long dossierId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bulletin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnneeAcademique() {
        return this.anneeAcademique;
    }

    public Bulletin anneeAcademique(String anneeAcademique) {
        this.setAnneeAcademique(anneeAcademique);
        return this;
    }

    public void setAnneeAcademique(String anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public Float getMoyenne() {
        return this.moyenne;
    }

    public Bulletin moyenne(Float moyenne) {
        this.setMoyenne(moyenne);
        return this;
    }

    public void setMoyenne(Float moyenne) {
        this.moyenne = moyenne;
    }

    public String getMention() {
        return this.mention;
    }

    public Bulletin mention(String mention) {
        this.setMention(mention);
        return this;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }

    public Long getDossierId() {
        return this.dossierId;
    }

    public Bulletin dossierId(Long dossierId) {
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
        if (!(o instanceof Bulletin)) {
            return false;
        }
        return getId() != null && getId().equals(((Bulletin) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bulletin{" +
            "id=" + getId() +
            ", anneeAcademique='" + getAnneeAcademique() + "'" +
            ", moyenne=" + getMoyenne() +
            ", mention='" + getMention() + "'" +
            ", dossierId=" + getDossierId() +
            "}";
    }
}
