package sn.edu.ugb.reporting.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.reporting.domain.RapportStatistique} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RapportStatistiqueDTO implements Serializable {

    private Long id;

    @NotNull
    private String titre;

    private String description;

    @NotNull
    private Instant dateGeneration;

    @NotNull
    private Long indicateurId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(Instant dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public Long getIndicateurId() {
        return indicateurId;
    }

    public void setIndicateurId(Long indicateurId) {
        this.indicateurId = indicateurId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RapportStatistiqueDTO)) {
            return false;
        }

        RapportStatistiqueDTO rapportStatistiqueDTO = (RapportStatistiqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rapportStatistiqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RapportStatistiqueDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateGeneration='" + getDateGeneration() + "'" +
            ", indicateurId=" + getIndicateurId() +
            "}";
    }
}
