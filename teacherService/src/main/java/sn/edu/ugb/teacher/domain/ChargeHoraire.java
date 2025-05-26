package sn.edu.ugb.teacher.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ChargeHoraire.
 */
@Entity
@Table(name = "charge_horaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChargeHoraire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre_heures", nullable = false)
    private Integer nombreHeures;

    @NotNull
    @Column(name = "contrat_id", nullable = false)
    private Long contratId;

    @NotNull
    @Column(name = "classe_id", nullable = false)
    private Long classeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChargeHoraire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNombreHeures() {
        return this.nombreHeures;
    }

    public ChargeHoraire nombreHeures(Integer nombreHeures) {
        this.setNombreHeures(nombreHeures);
        return this;
    }

    public void setNombreHeures(Integer nombreHeures) {
        this.nombreHeures = nombreHeures;
    }

    public Long getContratId() {
        return this.contratId;
    }

    public ChargeHoraire contratId(Long contratId) {
        this.setContratId(contratId);
        return this;
    }

    public void setContratId(Long contratId) {
        this.contratId = contratId;
    }

    public Long getClasseId() {
        return this.classeId;
    }

    public ChargeHoraire classeId(Long classeId) {
        this.setClasseId(classeId);
        return this;
    }

    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChargeHoraire)) {
            return false;
        }
        return getId() != null && getId().equals(((ChargeHoraire) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChargeHoraire{" +
            "id=" + getId() +
            ", nombreHeures=" + getNombreHeures() +
            ", contratId=" + getContratId() +
            ", classeId=" + getClasseId() +
            "}";
    }
}
