package sn.edu.ugb.reporting.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.reporting.domain.AccesRapport;
import sn.edu.ugb.reporting.repository.AccesRapportRepository;
import sn.edu.ugb.reporting.service.AccesRapportService;
import sn.edu.ugb.reporting.service.dto.AccesRapportDTO;
import sn.edu.ugb.reporting.service.mapper.AccesRapportMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.reporting.domain.AccesRapport}.
 */
@Service
@Transactional
public class AccesRapportServiceImpl implements AccesRapportService {

    private static final Logger LOG = LoggerFactory.getLogger(AccesRapportServiceImpl.class);

    private final AccesRapportRepository accesRapportRepository;

    private final AccesRapportMapper accesRapportMapper;

    public AccesRapportServiceImpl(AccesRapportRepository accesRapportRepository, AccesRapportMapper accesRapportMapper) {
        this.accesRapportRepository = accesRapportRepository;
        this.accesRapportMapper = accesRapportMapper;
    }

    @Override
    public AccesRapportDTO save(AccesRapportDTO accesRapportDTO) {
        LOG.debug("Request to save AccesRapport : {}", accesRapportDTO);
        AccesRapport accesRapport = accesRapportMapper.toEntity(accesRapportDTO);
        accesRapport = accesRapportRepository.save(accesRapport);
        return accesRapportMapper.toDto(accesRapport);
    }

    @Override
    public AccesRapportDTO update(AccesRapportDTO accesRapportDTO) {
        LOG.debug("Request to update AccesRapport : {}", accesRapportDTO);
        AccesRapport accesRapport = accesRapportMapper.toEntity(accesRapportDTO);
        accesRapport = accesRapportRepository.save(accesRapport);
        return accesRapportMapper.toDto(accesRapport);
    }

    @Override
    public Optional<AccesRapportDTO> partialUpdate(AccesRapportDTO accesRapportDTO) {
        LOG.debug("Request to partially update AccesRapport : {}", accesRapportDTO);

        return accesRapportRepository
            .findById(accesRapportDTO.getId())
            .map(existingAccesRapport -> {
                accesRapportMapper.partialUpdate(existingAccesRapport, accesRapportDTO);

                return existingAccesRapport;
            })
            .map(accesRapportRepository::save)
            .map(accesRapportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccesRapportDTO> findAll() {
        LOG.debug("Request to get all AccesRapports");
        return accesRapportRepository.findAll().stream().map(accesRapportMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccesRapportDTO> findOne(Long id) {
        LOG.debug("Request to get AccesRapport : {}", id);
        return accesRapportRepository.findById(id).map(accesRapportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AccesRapport : {}", id);
        accesRapportRepository.deleteById(id);
    }
}
