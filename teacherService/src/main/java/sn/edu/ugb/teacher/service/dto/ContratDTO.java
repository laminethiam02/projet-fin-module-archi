package sn.edu.ugb.teacher.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.teacher.domain.Contrat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContratDTO implements Serializable {

    private Long id;

    @NotNull
    private String typeContrat;

    @NotNull
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @NotNull
    private Long profilId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Long getProfilId() {
        return profilId;
    }

    public void setProfilId(Long profilId) {
        this.profilId = profilId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContratDTO)) {
            return false;
        }

        ContratDTO contratDTO = (ContratDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contratDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContratDTO{" +
            "id=" + getId() +
            ", typeContrat='" + getTypeContrat() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", profilId=" + getProfilId() +
            "}";
    }
}
