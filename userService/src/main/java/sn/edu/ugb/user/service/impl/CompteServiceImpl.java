package sn.edu.ugb.user.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.user.domain.Compte;
import sn.edu.ugb.user.repository.CompteRepository;
import sn.edu.ugb.user.service.CompteService;
import sn.edu.ugb.user.service.dto.CompteDTO;
import sn.edu.ugb.user.service.mapper.CompteMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.user.domain.Compte}.
 */
@Service
@Transactional
public class CompteServiceImpl implements CompteService {

    private static final Logger LOG = LoggerFactory.getLogger(CompteServiceImpl.class);

    private final CompteRepository compteRepository;

    private final CompteMapper compteMapper;

    public CompteServiceImpl(CompteRepository compteRepository, CompteMapper compteMapper) {
        this.compteRepository = compteRepository;
        this.compteMapper = compteMapper;
    }

    @Override
    public CompteDTO save(CompteDTO compteDTO) {
        LOG.debug("Request to save Compte : {}", compteDTO);
        Compte compte = compteMapper.toEntity(compteDTO);
        compte = compteRepository.save(compte);
        return compteMapper.toDto(compte);
    }

    @Override
    public CompteDTO update(CompteDTO compteDTO) {
        LOG.debug("Request to update Compte : {}", compteDTO);
        Compte compte = compteMapper.toEntity(compteDTO);
        compte = compteRepository.save(compte);
        return compteMapper.toDto(compte);
    }

    @Override
    public Optional<CompteDTO> partialUpdate(CompteDTO compteDTO) {
        LOG.debug("Request to partially update Compte : {}", compteDTO);

        return compteRepository
            .findById(compteDTO.getId())
            .map(existingCompte -> {
                compteMapper.partialUpdate(existingCompte, compteDTO);

                return existingCompte;
            })
            .map(compteRepository::save)
            .map(compteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Comptes");
        return compteRepository.findAll(pageable).map(compteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompteDTO> findOne(Long id) {
        LOG.debug("Request to get Compte : {}", id);
        return compteRepository.findById(id).map(compteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Compte : {}", id);
        compteRepository.deleteById(id);
    }
}
