package sn.edu.ugb.grade.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.grade.domain.Resultat;
import sn.edu.ugb.grade.repository.ResultatRepository;
import sn.edu.ugb.grade.service.ResultatService;
import sn.edu.ugb.grade.service.dto.ResultatDTO;
import sn.edu.ugb.grade.service.mapper.ResultatMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.grade.domain.Resultat}.
 */
@Service
@Transactional
public class ResultatServiceImpl implements ResultatService {

    private static final Logger LOG = LoggerFactory.getLogger(ResultatServiceImpl.class);

    private final ResultatRepository resultatRepository;

    private final ResultatMapper resultatMapper;

    public ResultatServiceImpl(ResultatRepository resultatRepository, ResultatMapper resultatMapper) {
        this.resultatRepository = resultatRepository;
        this.resultatMapper = resultatMapper;
    }

    @Override
    public ResultatDTO save(ResultatDTO resultatDTO) {
        LOG.debug("Request to save Resultat : {}", resultatDTO);
        Resultat resultat = resultatMapper.toEntity(resultatDTO);
        resultat = resultatRepository.save(resultat);
        return resultatMapper.toDto(resultat);
    }

    @Override
    public ResultatDTO update(ResultatDTO resultatDTO) {
        LOG.debug("Request to update Resultat : {}", resultatDTO);
        Resultat resultat = resultatMapper.toEntity(resultatDTO);
        resultat = resultatRepository.save(resultat);
        return resultatMapper.toDto(resultat);
    }

    @Override
    public Optional<ResultatDTO> partialUpdate(ResultatDTO resultatDTO) {
        LOG.debug("Request to partially update Resultat : {}", resultatDTO);

        return resultatRepository
            .findById(resultatDTO.getId())
            .map(existingResultat -> {
                resultatMapper.partialUpdate(existingResultat, resultatDTO);

                return existingResultat;
            })
            .map(resultatRepository::save)
            .map(resultatMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultatDTO> findAll() {
        LOG.debug("Request to get all Resultats");
        return resultatRepository.findAll().stream().map(resultatMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ResultatDTO> findOne(Long id) {
        LOG.debug("Request to get Resultat : {}", id);
        return resultatRepository.findById(id).map(resultatMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Resultat : {}", id);
        resultatRepository.deleteById(id);
    }
}
