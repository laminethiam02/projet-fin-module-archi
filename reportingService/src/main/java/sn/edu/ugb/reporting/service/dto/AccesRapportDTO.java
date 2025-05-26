package sn.edu.ugb.reporting.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.reporting.domain.AccesRapport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccesRapportDTO implements Serializable {

    private Long id;

    @NotNull
    private Long utilisateurId;

    @NotNull
    private Long rapportId;

    @NotNull
    private Instant dateAcces;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Long getRapportId() {
        return rapportId;
    }

    public void setRapportId(Long rapportId) {
        this.rapportId = rapportId;
    }

    public Instant getDateAcces() {
        return dateAcces;
    }

    public void setDateAcces(Instant dateAcces) {
        this.dateAcces = dateAcces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccesRapportDTO)) {
            return false;
        }

        AccesRapportDTO accesRapportDTO = (AccesRapportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accesRapportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccesRapportDTO{" +
            "id=" + getId() +
            ", utilisateurId=" + getUtilisateurId() +
            ", rapportId=" + getRapportId() +
            ", dateAcces='" + getDateAcces() + "'" +
            "}";
    }
}
