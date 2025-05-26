package sn.edu.ugb.curriculum.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UE.
 */
@Entity
@Table(name = "ue")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "intitule", nullable = false)
    private String intitule;

    @NotNull
    @Column(name = "credits", nullable = false)
    private Integer credits;

    @NotNull
    @Column(name = "niveau_id", nullable = false)
    private Long niveauId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UE id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return this.intitule;
    }

    public UE intitule(String intitule) {
        this.setIntitule(intitule);
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Integer getCredits() {
        return this.credits;
    }

    public UE credits(Integer credits) {
        this.setCredits(credits);
        return this;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Long getNiveauId() {
        return this.niveauId;
    }

    public UE niveauId(Long niveauId) {
        this.setNiveauId(niveauId);
        return this;
    }

    public void setNiveauId(Long niveauId) {
        this.niveauId = niveauId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UE)) {
            return false;
        }
        return getId() != null && getId().equals(((UE) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UE{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", credits=" + getCredits() +
            ", niveauId=" + getNiveauId() +
            "}";
    }
}
