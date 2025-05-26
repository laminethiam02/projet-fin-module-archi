package sn.edu.ugb.grade.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.grade.domain.Bareme;
import sn.edu.ugb.grade.repository.BaremeRepository;
import sn.edu.ugb.grade.service.BaremeService;
import sn.edu.ugb.grade.service.dto.BaremeDTO;
import sn.edu.ugb.grade.service.mapper.BaremeMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.grade.domain.Bareme}.
 */
@Service
@Transactional
public class BaremeServiceImpl implements BaremeService {

    private static final Logger LOG = LoggerFactory.getLogger(BaremeServiceImpl.class);

    private final BaremeRepository baremeRepository;

    private final BaremeMapper baremeMapper;

    public BaremeServiceImpl(BaremeRepository baremeRepository, BaremeMapper baremeMapper) {
        this.baremeRepository = baremeRepository;
        this.baremeMapper = baremeMapper;
    }

    @Override
    public BaremeDTO save(BaremeDTO baremeDTO) {
        LOG.debug("Request to save Bareme : {}", baremeDTO);
        Bareme bareme = baremeMapper.toEntity(baremeDTO);
        bareme = baremeRepository.save(bareme);
        return baremeMapper.toDto(bareme);
    }

    @Override
    public BaremeDTO update(BaremeDTO baremeDTO) {
        LOG.debug("Request to update Bareme : {}", baremeDTO);
        Bareme bareme = baremeMapper.toEntity(baremeDTO);
        bareme = baremeRepository.save(bareme);
        return baremeMapper.toDto(bareme);
    }

    @Override
    public Optional<BaremeDTO> partialUpdate(BaremeDTO baremeDTO) {
        LOG.debug("Request to partially update Bareme : {}", baremeDTO);

        return baremeRepository
            .findById(baremeDTO.getId())
            .map(existingBareme -> {
                baremeMapper.partialUpdate(existingBareme, baremeDTO);

                return existingBareme;
            })
            .map(baremeRepository::save)
            .map(baremeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BaremeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Baremes");
        return baremeRepository.findAll(pageable).map(baremeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BaremeDTO> findOne(Long id) {
        LOG.debug("Request to get Bareme : {}", id);
        return baremeRepository.findById(id).map(baremeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Bareme : {}", id);
        baremeRepository.deleteById(id);
    }
}
