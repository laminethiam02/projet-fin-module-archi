package sn.edu.ugb.reporting.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RapportStatistique.
 */
@Entity
@Table(name = "rapport_statistique")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RapportStatistique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "date_generation", nullable = false)
    private Instant dateGeneration;

    @NotNull
    @Column(name = "indicateur_id", nullable = false)
    private Long indicateurId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RapportStatistique id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public RapportStatistique titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return this.description;
    }

    public RapportStatistique description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateGeneration() {
        return this.dateGeneration;
    }

    public RapportStatistique dateGeneration(Instant dateGeneration) {
        this.setDateGeneration(dateGeneration);
        return this;
    }

    public void setDateGeneration(Instant dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public Long getIndicateurId() {
        return this.indicateurId;
    }

    public RapportStatistique indicateurId(Long indicateurId) {
        this.setIndicateurId(indicateurId);
        return this;
    }

    public void setIndicateurId(Long indicateurId) {
        this.indicateurId = indicateurId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RapportStatistique)) {
            return false;
        }
        return getId() != null && getId().equals(((RapportStatistique) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RapportStatistique{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateGeneration='" + getDateGeneration() + "'" +
            ", indicateurId=" + getIndicateurId() +
            "}";
    }
}
