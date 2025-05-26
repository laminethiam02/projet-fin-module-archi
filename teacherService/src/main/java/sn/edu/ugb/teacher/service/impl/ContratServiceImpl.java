package sn.edu.ugb.teacher.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.teacher.domain.Contrat;
import sn.edu.ugb.teacher.repository.ContratRepository;
import sn.edu.ugb.teacher.service.ContratService;
import sn.edu.ugb.teacher.service.dto.ContratDTO;
import sn.edu.ugb.teacher.service.mapper.ContratMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.teacher.domain.Contrat}.
 */
@Service
@Transactional
public class ContratServiceImpl implements ContratService {

    private static final Logger LOG = LoggerFactory.getLogger(ContratServiceImpl.class);

    private final ContratRepository contratRepository;

    private final ContratMapper contratMapper;

    public ContratServiceImpl(ContratRepository contratRepository, ContratMapper contratMapper) {
        this.contratRepository = contratRepository;
        this.contratMapper = contratMapper;
    }

    @Override
    public ContratDTO save(ContratDTO contratDTO) {
        LOG.debug("Request to save Contrat : {}", contratDTO);
        Contrat contrat = contratMapper.toEntity(contratDTO);
        contrat = contratRepository.save(contrat);
        return contratMapper.toDto(contrat);
    }

    @Override
    public ContratDTO update(ContratDTO contratDTO) {
        LOG.debug("Request to update Contrat : {}", contratDTO);
        Contrat contrat = contratMapper.toEntity(contratDTO);
        contrat = contratRepository.save(contrat);
        return contratMapper.toDto(contrat);
    }

    @Override
    public Optional<ContratDTO> partialUpdate(ContratDTO contratDTO) {
        LOG.debug("Request to partially update Contrat : {}", contratDTO);

        return contratRepository
            .findById(contratDTO.getId())
            .map(existingContrat -> {
                contratMapper.partialUpdate(existingContrat, contratDTO);

                return existingContrat;
            })
            .map(contratRepository::save)
            .map(contratMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContratDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Contrats");
        return contratRepository.findAll(pageable).map(contratMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContratDTO> findOne(Long id) {
        LOG.debug("Request to get Contrat : {}", id);
        return contratRepository.findById(id).map(contratMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Contrat : {}", id);
        contratRepository.deleteById(id);
    }
}
