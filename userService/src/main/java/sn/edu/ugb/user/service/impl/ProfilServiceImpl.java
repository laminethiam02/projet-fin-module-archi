package sn.edu.ugb.user.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.user.domain.Profil;
import sn.edu.ugb.user.repository.ProfilRepository;
import sn.edu.ugb.user.service.ProfilService;
import sn.edu.ugb.user.service.dto.ProfilDTO;
import sn.edu.ugb.user.service.mapper.ProfilMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.user.domain.Profil}.
 */
@Service
@Transactional
public class ProfilServiceImpl implements ProfilService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfilServiceImpl.class);

    private final ProfilRepository profilRepository;

    private final ProfilMapper profilMapper;

    public ProfilServiceImpl(ProfilRepository profilRepository, ProfilMapper profilMapper) {
        this.profilRepository = profilRepository;
        this.profilMapper = profilMapper;
    }

    @Override
    public ProfilDTO save(ProfilDTO profilDTO) {
        LOG.debug("Request to save Profil : {}", profilDTO);
        Profil profil = profilMapper.toEntity(profilDTO);
        profil = profilRepository.save(profil);
        return profilMapper.toDto(profil);
    }

    @Override
    public ProfilDTO update(ProfilDTO profilDTO) {
        LOG.debug("Request to update Profil : {}", profilDTO);
        Profil profil = profilMapper.toEntity(profilDTO);
        profil = profilRepository.save(profil);
        return profilMapper.toDto(profil);
    }

    @Override
    public Optional<ProfilDTO> partialUpdate(ProfilDTO profilDTO) {
        LOG.debug("Request to partially update Profil : {}", profilDTO);

        return profilRepository
            .findById(profilDTO.getId())
            .map(existingProfil -> {
                profilMapper.partialUpdate(existingProfil, profilDTO);

                return existingProfil;
            })
            .map(profilRepository::save)
            .map(profilMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfilDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Profils");
        return profilRepository.findAll(pageable).map(profilMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfilDTO> findOne(Long id) {
        LOG.debug("Request to get Profil : {}", id);
        return profilRepository.findById(id).map(profilMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Profil : {}", id);
        profilRepository.deleteById(id);
    }
}
