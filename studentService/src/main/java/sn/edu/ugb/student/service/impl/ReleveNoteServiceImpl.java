package sn.edu.ugb.student.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.student.domain.ReleveNote;
import sn.edu.ugb.student.repository.ReleveNoteRepository;
import sn.edu.ugb.student.service.ReleveNoteService;
import sn.edu.ugb.student.service.dto.ReleveNoteDTO;
import sn.edu.ugb.student.service.mapper.ReleveNoteMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.student.domain.ReleveNote}.
 */
@Service
@Transactional
public class ReleveNoteServiceImpl implements ReleveNoteService {

    private static final Logger LOG = LoggerFactory.getLogger(ReleveNoteServiceImpl.class);

    private final ReleveNoteRepository releveNoteRepository;

    private final ReleveNoteMapper releveNoteMapper;

    public ReleveNoteServiceImpl(ReleveNoteRepository releveNoteRepository, ReleveNoteMapper releveNoteMapper) {
        this.releveNoteRepository = releveNoteRepository;
        this.releveNoteMapper = releveNoteMapper;
    }

    @Override
    public ReleveNoteDTO save(ReleveNoteDTO releveNoteDTO) {
        LOG.debug("Request to save ReleveNote : {}", releveNoteDTO);
        ReleveNote releveNote = releveNoteMapper.toEntity(releveNoteDTO);
        releveNote = releveNoteRepository.save(releveNote);
        return releveNoteMapper.toDto(releveNote);
    }

    @Override
    public ReleveNoteDTO update(ReleveNoteDTO releveNoteDTO) {
        LOG.debug("Request to update ReleveNote : {}", releveNoteDTO);
        ReleveNote releveNote = releveNoteMapper.toEntity(releveNoteDTO);
        releveNote = releveNoteRepository.save(releveNote);
        return releveNoteMapper.toDto(releveNote);
    }

    @Override
    public Optional<ReleveNoteDTO> partialUpdate(ReleveNoteDTO releveNoteDTO) {
        LOG.debug("Request to partially update ReleveNote : {}", releveNoteDTO);

        return releveNoteRepository
            .findById(releveNoteDTO.getId())
            .map(existingReleveNote -> {
                releveNoteMapper.partialUpdate(existingReleveNote, releveNoteDTO);

                return existingReleveNote;
            })
            .map(releveNoteRepository::save)
            .map(releveNoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReleveNoteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ReleveNotes");
        return releveNoteRepository.findAll(pageable).map(releveNoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReleveNoteDTO> findOne(Long id) {
        LOG.debug("Request to get ReleveNote : {}", id);
        return releveNoteRepository.findById(id).map(releveNoteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReleveNote : {}", id);
        releveNoteRepository.deleteById(id);
    }
}
