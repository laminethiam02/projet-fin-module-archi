package sn.edu.ugb.curriculum.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Programme.
 */
@Entity
@Table(name = "programme")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Programme implements Serializable {

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
    @Column(name = "code_programme", nullable = false, unique = true)
    private String codeProgramme;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Programme id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return this.intitule;
    }

    public Programme intitule(String intitule) {
        this.setIntitule(intitule);
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getCodeProgramme() {
        return this.codeProgramme;
    }

    public Programme codeProgramme(String codeProgramme) {
        this.setCodeProgramme(codeProgramme);
        return this;
    }

    public void setCodeProgramme(String codeProgramme) {
        this.codeProgramme = codeProgramme;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Programme)) {
            return false;
        }
        return getId() != null && getId().equals(((Programme) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Programme{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", codeProgramme='" + getCodeProgramme() + "'" +
            "}";
    }
}
