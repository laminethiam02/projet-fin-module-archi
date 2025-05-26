package sn.edu.ugb.student.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DossierEtudiant.
 */
@Entity
@Table(name = "dossier_etudiant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DossierEtudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "matricule", nullable = false, unique = true)
    private String matricule;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @NotNull
    @Column(name = "profil_id", nullable = false)
    private Long profilId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DossierEtudiant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return this.matricule;
    }

    public DossierEtudiant matricule(String matricule) {
        this.setMatricule(matricule);
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNationalite() {
        return this.nationalite;
    }

    public DossierEtudiant nationalite(String nationalite) {
        this.setNationalite(nationalite);
        return this;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    public DossierEtudiant dateNaissance(LocalDate dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Long getProfilId() {
        return this.profilId;
    }

    public DossierEtudiant profilId(Long profilId) {
        this.setProfilId(profilId);
        return this;
    }

    public void setProfilId(Long profilId) {
        this.profilId = profilId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DossierEtudiant)) {
            return false;
        }
        return getId() != null && getId().equals(((DossierEtudiant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DossierEtudiant{" +
            "id=" + getId() +
            ", matricule='" + getMatricule() + "'" +
            ", nationalite='" + getNationalite() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", profilId=" + getProfilId() +
            "}";
    }
}
