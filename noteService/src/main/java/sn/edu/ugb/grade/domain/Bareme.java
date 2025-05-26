package sn.edu.ugb.grade.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bareme.
 */
@Entity
@Table(name = "bareme")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bareme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "intitule")
    private String intitule;

    @NotNull
    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;

    @NotNull
    @Column(name = "examen_id", nullable = false)
    private Long examenId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bareme id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return this.intitule;
    }

    public Bareme intitule(String intitule) {
        this.setIntitule(intitule);
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Integer getTotalPoints() {
        return this.totalPoints;
    }

    public Bareme totalPoints(Integer totalPoints) {
        this.setTotalPoints(totalPoints);
        return this;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Long getExamenId() {
        return this.examenId;
    }

    public Bareme examenId(Long examenId) {
        this.setExamenId(examenId);
        return this;
    }

    public void setExamenId(Long examenId) {
        this.examenId = examenId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bareme)) {
            return false;
        }
        return getId() != null && getId().equals(((Bareme) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bareme{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", totalPoints=" + getTotalPoints() +
            ", examenId=" + getExamenId() +
            "}";
    }
}
