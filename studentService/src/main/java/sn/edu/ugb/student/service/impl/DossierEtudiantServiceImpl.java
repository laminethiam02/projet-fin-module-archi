package sn.edu.ugb.student.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.student.domain.DossierEtudiant;
import sn.edu.ugb.student.repository.DossierEtudiantRepository;
import sn.edu.ugb.student.service.DossierEtudiantService;
import sn.edu.ugb.student.service.dto.DossierEtudiantDTO;
import sn.edu.ugb.student.service.mapper.DossierEtudiantMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.student.domain.DossierEtudiant}.
 */
@Service
@Transactional
public class DossierEtudiantServiceImpl implements DossierEtudiantService {

    private static final Logger LOG = LoggerFactory.getLogger(DossierEtudiantServiceImpl.class);

    private final DossierEtudiantRepository dossierEtudiantRepository;

    private final DossierEtudiantMapper dossierEtudiantMapper;

    public DossierEtudiantServiceImpl(DossierEtudiantRepository dossierEtudiantRepository, DossierEtudiantMapper dossierEtudiantMapper) {
        this.dossierEtudiantRepository = dossierEtudiantRepository;
        this.dossierEtudiantMapper = dossierEtudiantMapper;
    }

    @Override
    public DossierEtudiantDTO save(DossierEtudiantDTO dossierEtudiantDTO) {
        LOG.debug("Request to save DossierEtudiant : {}", dossierEtudiantDTO);
        DossierEtudiant dossierEtudiant = dossierEtudiantMapper.toEntity(dossierEtudiantDTO);
        dossierEtudiant = dossierEtudiantRepository.save(dossierEtudiant);
        return dossierEtudiantMapper.toDto(dossierEtudiant);
    }

    @Override
    public DossierEtudiantDTO update(DossierEtudiantDTO dossierEtudiantDTO) {
        LOG.debug("Request to update DossierEtudiant : {}", dossierEtudiantDTO);
        DossierEtudiant dossierEtudiant = dossierEtudiantMapper.toEntity(dossierEtudiantDTO);
        dossierEtudiant = dossierEtudiantRepository.save(dossierEtudiant);
        return dossierEtudiantMapper.toDto(dossierEtudiant);
    }

    @Override
    public Optional<DossierEtudiantDTO> partialUpdate(DossierEtudiantDTO dossierEtudiantDTO) {
        LOG.debug("Request to partially update DossierEtudiant : {}", dossierEtudiantDTO);

        return dossierEtudiantRepository
            .findById(dossierEtudiantDTO.getId())
            .map(existingDossierEtudiant -> {
                dossierEtudiantMapper.partialUpdate(existingDossierEtudiant, dossierEtudiantDTO);

                return existingDossierEtudiant;
            })
            .map(dossierEtudiantRepository::save)
            .map(dossierEtudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DossierEtudiantDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DossierEtudiants");
        return dossierEtudiantRepository.findAll(pageable).map(dossierEtudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DossierEtudiantDTO> findOne(Long id) {
        LOG.debug("Request to get DossierEtudiant : {}", id);
        return dossierEtudiantRepository.findById(id).map(dossierEtudiantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DossierEtudiant : {}", id);
        dossierEtudiantRepository.deleteById(id);
    }
}
