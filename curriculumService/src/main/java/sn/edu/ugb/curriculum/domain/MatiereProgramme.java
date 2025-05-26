package sn.edu.ugb.curriculum.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MatiereProgramme.
 */
@Entity
@Table(name = "matiere_programme")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MatiereProgramme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_matiere", nullable = false)
    private String nomMatiere;

    @Column(name = "heures_cours")
    private Integer heuresCours;

    @NotNull
    @Column(name = "ue_id", nullable = false)
    private Long ueId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MatiereProgramme id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomMatiere() {
        return this.nomMatiere;
    }

    public MatiereProgramme nomMatiere(String nomMatiere) {
        this.setNomMatiere(nomMatiere);
        return this;
    }

    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    public Integer getHeuresCours() {
        return this.heuresCours;
    }

    public MatiereProgramme heuresCours(Integer heuresCours) {
        this.setHeuresCours(heuresCours);
        return this;
    }

    public void setHeuresCours(Integer heuresCours) {
        this.heuresCours = heuresCours;
    }

    public Long getUeId() {
        return this.ueId;
    }

    public MatiereProgramme ueId(Long ueId) {
        this.setUeId(ueId);
        return this;
    }

    public void setUeId(Long ueId) {
        this.ueId = ueId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatiereProgramme)) {
            return false;
        }
        return getId() != null && getId().equals(((MatiereProgramme) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatiereProgramme{" +
            "id=" + getId() +
            ", nomMatiere='" + getNomMatiere() + "'" +
            ", heuresCours=" + getHeuresCours() +
            ", ueId=" + getUeId() +
            "}";
    }
}
