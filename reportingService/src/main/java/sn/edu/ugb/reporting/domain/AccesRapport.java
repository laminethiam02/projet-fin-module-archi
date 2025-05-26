package sn.edu.ugb.reporting.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AccesRapport.
 */
@Entity
@Table(name = "acces_rapport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccesRapport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "utilisateur_id", nullable = false)
    private Long utilisateurId;

    @NotNull
    @Column(name = "rapport_id", nullable = false)
    private Long rapportId;

    @NotNull
    @Column(name = "date_acces", nullable = false)
    private Instant dateAcces;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccesRapport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUtilisateurId() {
        return this.utilisateurId;
    }

    public AccesRapport utilisateurId(Long utilisateurId) {
        this.setUtilisateurId(utilisateurId);
        return this;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Long getRapportId() {
        return this.rapportId;
    }

    public AccesRapport rapportId(Long rapportId) {
        this.setRapportId(rapportId);
        return this;
    }

    public void setRapportId(Long rapportId) {
        this.rapportId = rapportId;
    }

    public Instant getDateAcces() {
        return this.dateAcces;
    }

    public AccesRapport dateAcces(Instant dateAcces) {
        this.setDateAcces(dateAcces);
        return this;
    }

    public void setDateAcces(Instant dateAcces) {
        this.dateAcces = dateAcces;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccesRapport)) {
            return false;
        }
        return getId() != null && getId().equals(((AccesRapport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccesRapport{" +
            "id=" + getId() +
            ", utilisateurId=" + getUtilisateurId() +
            ", rapportId=" + getRapportId() +
            ", dateAcces='" + getDateAcces() + "'" +
            "}";
    }
}
