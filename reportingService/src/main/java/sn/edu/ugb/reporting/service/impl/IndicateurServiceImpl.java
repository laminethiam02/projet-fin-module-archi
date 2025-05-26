package sn.edu.ugb.reporting.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.reporting.domain.Indicateur;
import sn.edu.ugb.reporting.repository.IndicateurRepository;
import sn.edu.ugb.reporting.service.IndicateurService;
import sn.edu.ugb.reporting.service.dto.IndicateurDTO;
import sn.edu.ugb.reporting.service.mapper.IndicateurMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.reporting.domain.Indicateur}.
 */
@Service
@Transactional
public class IndicateurServiceImpl implements IndicateurService {

    private static final Logger LOG = LoggerFactory.getLogger(IndicateurServiceImpl.class);

    private final IndicateurRepository indicateurRepository;

    private final IndicateurMapper indicateurMapper;

    public IndicateurServiceImpl(IndicateurRepository indicateurRepository, IndicateurMapper indicateurMapper) {
        this.indicateurRepository = indicateurRepository;
        this.indicateurMapper = indicateurMapper;
    }

    @Override
    public IndicateurDTO save(IndicateurDTO indicateurDTO) {
        LOG.debug("Request to save Indicateur : {}", indicateurDTO);
        Indicateur indicateur = indicateurMapper.toEntity(indicateurDTO);
        indicateur = indicateurRepository.save(indicateur);
        return indicateurMapper.toDto(indicateur);
    }

    @Override
    public IndicateurDTO update(IndicateurDTO indicateurDTO) {
        LOG.debug("Request to update Indicateur : {}", indicateurDTO);
        Indicateur indicateur = indicateurMapper.toEntity(indicateurDTO);
        indicateur = indicateurRepository.save(indicateur);
        return indicateurMapper.toDto(indicateur);
    }

    @Override
    public Optional<IndicateurDTO> partialUpdate(IndicateurDTO indicateurDTO) {
        LOG.debug("Request to partially update Indicateur : {}", indicateurDTO);

        return indicateurRepository
            .findById(indicateurDTO.getId())
            .map(existingIndicateur -> {
                indicateurMapper.partialUpdate(existingIndicateur, indicateurDTO);

                return existingIndicateur;
            })
            .map(indicateurRepository::save)
            .map(indicateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IndicateurDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Indicateurs");
        return indicateurRepository.findAll(pageable).map(indicateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IndicateurDTO> findOne(Long id) {
        LOG.debug("Request to get Indicateur : {}", id);
        return indicateurRepository.findById(id).map(indicateurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Indicateur : {}", id);
        indicateurRepository.deleteById(id);
    }
}
