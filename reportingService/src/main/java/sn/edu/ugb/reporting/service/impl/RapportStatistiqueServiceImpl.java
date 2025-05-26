package sn.edu.ugb.reporting.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.reporting.domain.RapportStatistique;
import sn.edu.ugb.reporting.repository.RapportStatistiqueRepository;
import sn.edu.ugb.reporting.service.RapportStatistiqueService;
import sn.edu.ugb.reporting.service.dto.RapportStatistiqueDTO;
import sn.edu.ugb.reporting.service.mapper.RapportStatistiqueMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.reporting.domain.RapportStatistique}.
 */
@Service
@Transactional
public class RapportStatistiqueServiceImpl implements RapportStatistiqueService {

    private static final Logger LOG = LoggerFactory.getLogger(RapportStatistiqueServiceImpl.class);

    private final RapportStatistiqueRepository rapportStatistiqueRepository;

    private final RapportStatistiqueMapper rapportStatistiqueMapper;

    public RapportStatistiqueServiceImpl(
        RapportStatistiqueRepository rapportStatistiqueRepository,
        RapportStatistiqueMapper rapportStatistiqueMapper
    ) {
        this.rapportStatistiqueRepository = rapportStatistiqueRepository;
        this.rapportStatistiqueMapper = rapportStatistiqueMapper;
    }

    @Override
    public RapportStatistiqueDTO save(RapportStatistiqueDTO rapportStatistiqueDTO) {
        LOG.debug("Request to save RapportStatistique : {}", rapportStatistiqueDTO);
        RapportStatistique rapportStatistique = rapportStatistiqueMapper.toEntity(rapportStatistiqueDTO);
        rapportStatistique = rapportStatistiqueRepository.save(rapportStatistique);
        return rapportStatistiqueMapper.toDto(rapportStatistique);
    }

    @Override
    public RapportStatistiqueDTO update(RapportStatistiqueDTO rapportStatistiqueDTO) {
        LOG.debug("Request to update RapportStatistique : {}", rapportStatistiqueDTO);
        RapportStatistique rapportStatistique = rapportStatistiqueMapper.toEntity(rapportStatistiqueDTO);
        rapportStatistique = rapportStatistiqueRepository.save(rapportStatistique);
        return rapportStatistiqueMapper.toDto(rapportStatistique);
    }

    @Override
    public Optional<RapportStatistiqueDTO> partialUpdate(RapportStatistiqueDTO rapportStatistiqueDTO) {
        LOG.debug("Request to partially update RapportStatistique : {}", rapportStatistiqueDTO);

        return rapportStatistiqueRepository
            .findById(rapportStatistiqueDTO.getId())
            .map(existingRapportStatistique -> {
                rapportStatistiqueMapper.partialUpdate(existingRapportStatistique, rapportStatistiqueDTO);

                return existingRapportStatistique;
            })
            .map(rapportStatistiqueRepository::save)
            .map(rapportStatistiqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RapportStatistiqueDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RapportStatistiques");
        return rapportStatistiqueRepository.findAll(pageable).map(rapportStatistiqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RapportStatistiqueDTO> findOne(Long id) {
        LOG.debug("Request to get RapportStatistique : {}", id);
        return rapportStatistiqueRepository.findById(id).map(rapportStatistiqueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete RapportStatistique : {}", id);
        rapportStatistiqueRepository.deleteById(id);
    }
}
