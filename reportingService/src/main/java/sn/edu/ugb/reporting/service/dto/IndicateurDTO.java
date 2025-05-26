package sn.edu.ugb.reporting.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.reporting.domain.Indicateur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndicateurDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private Float valeur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Float getValeur() {
        return valeur;
    }

    public void setValeur(Float valeur) {
        this.valeur = valeur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndicateurDTO)) {
            return false;
        }

        IndicateurDTO indicateurDTO = (IndicateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, indicateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndicateurDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", valeur=" + getValeur() +
            "}";
    }
}
