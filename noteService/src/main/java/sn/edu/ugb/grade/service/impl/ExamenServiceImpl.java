package sn.edu.ugb.grade.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.grade.domain.Examen;
import sn.edu.ugb.grade.repository.ExamenRepository;
import sn.edu.ugb.grade.service.ExamenService;
import sn.edu.ugb.grade.service.dto.ExamenDTO;
import sn.edu.ugb.grade.service.mapper.ExamenMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.grade.domain.Examen}.
 */
@Service
@Transactional
public class ExamenServiceImpl implements ExamenService {

    private static final Logger LOG = LoggerFactory.getLogger(ExamenServiceImpl.class);

    private final ExamenRepository examenRepository;

    private final ExamenMapper examenMapper;

    public ExamenServiceImpl(ExamenRepository examenRepository, ExamenMapper examenMapper) {
        this.examenRepository = examenRepository;
        this.examenMapper = examenMapper;
    }

    @Override
    public ExamenDTO save(ExamenDTO examenDTO) {
        LOG.debug("Request to save Examen : {}", examenDTO);
        Examen examen = examenMapper.toEntity(examenDTO);
        examen = examenRepository.save(examen);
        return examenMapper.toDto(examen);
    }

    @Override
    public ExamenDTO update(ExamenDTO examenDTO) {
        LOG.debug("Request to update Examen : {}", examenDTO);
        Examen examen = examenMapper.toEntity(examenDTO);
        examen = examenRepository.save(examen);
        return examenMapper.toDto(examen);
    }

    @Override
    public Optional<ExamenDTO> partialUpdate(ExamenDTO examenDTO) {
        LOG.debug("Request to partially update Examen : {}", examenDTO);

        return examenRepository
            .findById(examenDTO.getId())
            .map(existingExamen -> {
                examenMapper.partialUpdate(existingExamen, examenDTO);

                return existingExamen;
            })
            .map(examenRepository::save)
            .map(examenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamenDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Examen");
        return examenRepository.findAll(pageable).map(examenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExamenDTO> findOne(Long id) {
        LOG.debug("Request to get Examen : {}", id);
        return examenRepository.findById(id).map(examenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Examen : {}", id);
        examenRepository.deleteById(id);
    }
}
