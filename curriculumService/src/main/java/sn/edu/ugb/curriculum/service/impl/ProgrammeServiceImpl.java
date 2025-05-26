package sn.edu.ugb.curriculum.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.curriculum.domain.Programme;
import sn.edu.ugb.curriculum.repository.ProgrammeRepository;
import sn.edu.ugb.curriculum.service.ProgrammeService;
import sn.edu.ugb.curriculum.service.dto.ProgrammeDTO;
import sn.edu.ugb.curriculum.service.mapper.ProgrammeMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.curriculum.domain.Programme}.
 */
@Service
@Transactional
public class ProgrammeServiceImpl implements ProgrammeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProgrammeServiceImpl.class);

    private final ProgrammeRepository programmeRepository;

    private final ProgrammeMapper programmeMapper;

    public ProgrammeServiceImpl(ProgrammeRepository programmeRepository, ProgrammeMapper programmeMapper) {
        this.programmeRepository = programmeRepository;
        this.programmeMapper = programmeMapper;
    }

    @Override
    public ProgrammeDTO save(ProgrammeDTO programmeDTO) {
        LOG.debug("Request to save Programme : {}", programmeDTO);
        Programme programme = programmeMapper.toEntity(programmeDTO);
        programme = programmeRepository.save(programme);
        return programmeMapper.toDto(programme);
    }

    @Override
    public ProgrammeDTO update(ProgrammeDTO programmeDTO) {
        LOG.debug("Request to update Programme : {}", programmeDTO);
        Programme programme = programmeMapper.toEntity(programmeDTO);
        programme = programmeRepository.save(programme);
        return programmeMapper.toDto(programme);
    }

    @Override
    public Optional<ProgrammeDTO> partialUpdate(ProgrammeDTO programmeDTO) {
        LOG.debug("Request to partially update Programme : {}", programmeDTO);

        return programmeRepository
            .findById(programmeDTO.getId())
            .map(existingProgramme -> {
                programmeMapper.partialUpdate(existingProgramme, programmeDTO);

                return existingProgramme;
            })
            .map(programmeRepository::save)
            .map(programmeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProgrammeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Programmes");
        return programmeRepository.findAll(pageable).map(programmeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgrammeDTO> findOne(Long id) {
        LOG.debug("Request to get Programme : {}", id);
        return programmeRepository.findById(id).map(programmeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Programme : {}", id);
        programmeRepository.deleteById(id);
    }
}
