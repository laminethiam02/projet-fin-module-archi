package sn.edu.ugb.curriculum.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.curriculum.domain.MatiereProgramme;
import sn.edu.ugb.curriculum.repository.MatiereProgrammeRepository;
import sn.edu.ugb.curriculum.service.MatiereProgrammeService;
import sn.edu.ugb.curriculum.service.dto.MatiereProgrammeDTO;
import sn.edu.ugb.curriculum.service.mapper.MatiereProgrammeMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.curriculum.domain.MatiereProgramme}.
 */
@Service
@Transactional
public class MatiereProgrammeServiceImpl implements MatiereProgrammeService {

    private static final Logger LOG = LoggerFactory.getLogger(MatiereProgrammeServiceImpl.class);

    private final MatiereProgrammeRepository matiereProgrammeRepository;

    private final MatiereProgrammeMapper matiereProgrammeMapper;

    public MatiereProgrammeServiceImpl(
        MatiereProgrammeRepository matiereProgrammeRepository,
        MatiereProgrammeMapper matiereProgrammeMapper
    ) {
        this.matiereProgrammeRepository = matiereProgrammeRepository;
        this.matiereProgrammeMapper = matiereProgrammeMapper;
    }

    @Override
    public MatiereProgrammeDTO save(MatiereProgrammeDTO matiereProgrammeDTO) {
        LOG.debug("Request to save MatiereProgramme : {}", matiereProgrammeDTO);
        MatiereProgramme matiereProgramme = matiereProgrammeMapper.toEntity(matiereProgrammeDTO);
        matiereProgramme = matiereProgrammeRepository.save(matiereProgramme);
        return matiereProgrammeMapper.toDto(matiereProgramme);
    }

    @Override
    public MatiereProgrammeDTO update(MatiereProgrammeDTO matiereProgrammeDTO) {
        LOG.debug("Request to update MatiereProgramme : {}", matiereProgrammeDTO);
        MatiereProgramme matiereProgramme = matiereProgrammeMapper.toEntity(matiereProgrammeDTO);
        matiereProgramme = matiereProgrammeRepository.save(matiereProgramme);
        return matiereProgrammeMapper.toDto(matiereProgramme);
    }

    @Override
    public Optional<MatiereProgrammeDTO> partialUpdate(MatiereProgrammeDTO matiereProgrammeDTO) {
        LOG.debug("Request to partially update MatiereProgramme : {}", matiereProgrammeDTO);

        return matiereProgrammeRepository
            .findById(matiereProgrammeDTO.getId())
            .map(existingMatiereProgramme -> {
                matiereProgrammeMapper.partialUpdate(existingMatiereProgramme, matiereProgrammeDTO);

                return existingMatiereProgramme;
            })
            .map(matiereProgrammeRepository::save)
            .map(matiereProgrammeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MatiereProgrammeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MatiereProgrammes");
        return matiereProgrammeRepository.findAll(pageable).map(matiereProgrammeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MatiereProgrammeDTO> findOne(Long id) {
        LOG.debug("Request to get MatiereProgramme : {}", id);
        return matiereProgrammeRepository.findById(id).map(matiereProgrammeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MatiereProgramme : {}", id);
        matiereProgrammeRepository.deleteById(id);
    }
}
