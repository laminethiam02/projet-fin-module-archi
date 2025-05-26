package sn.edu.ugb.user.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.edu.ugb.user.domain.Compte} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteDTO implements Serializable {

    private Long id;

    @NotNull
    private String login;

    @NotNull
    private String motDePasse;

    private Boolean actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteDTO)) {
            return false;
        }

        CompteDTO compteDTO = (CompteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteDTO{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", motDePasse='" + getMotDePasse() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
