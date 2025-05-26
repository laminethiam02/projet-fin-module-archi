package sn.edu.ugb.teacher.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.teacher.domain.Classe;
import sn.edu.ugb.teacher.repository.ClasseRepository;
import sn.edu.ugb.teacher.service.ClasseService;
import sn.edu.ugb.teacher.service.dto.ClasseDTO;
import sn.edu.ugb.teacher.service.mapper.ClasseMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.teacher.domain.Classe}.
 */
@Service
@Transactional
public class ClasseServiceImpl implements ClasseService {

    private static final Logger LOG = LoggerFactory.getLogger(ClasseServiceImpl.class);

    private final ClasseRepository classeRepository;

    private final ClasseMapper classeMapper;

    public ClasseServiceImpl(ClasseRepository classeRepository, ClasseMapper classeMapper) {
        this.classeRepository = classeRepository;
        this.classeMapper = classeMapper;
    }

    @Override
    public ClasseDTO save(ClasseDTO classeDTO) {
        LOG.debug("Request to save Classe : {}", classeDTO);
        Classe classe = classeMapper.toEntity(classeDTO);
        classe = classeRepository.save(classe);
        return classeMapper.toDto(classe);
    }

    @Override
    public ClasseDTO update(ClasseDTO classeDTO) {
        LOG.debug("Request to update Classe : {}", classeDTO);
        Classe classe = classeMapper.toEntity(classeDTO);
        classe = classeRepository.save(classe);
        return classeMapper.toDto(classe);
    }

    @Override
    public Optional<ClasseDTO> partialUpdate(ClasseDTO classeDTO) {
        LOG.debug("Request to partially update Classe : {}", classeDTO);

        return classeRepository
            .findById(classeDTO.getId())
            .map(existingClasse -> {
                classeMapper.partialUpdate(existingClasse, classeDTO);

                return existingClasse;
            })
            .map(classeRepository::save)
            .map(classeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClasseDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Classes");
        return classeRepository.findAll(pageable).map(classeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClasseDTO> findOne(Long id) {
        LOG.debug("Request to get Classe : {}", id);
        return classeRepository.findById(id).map(classeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Classe : {}", id);
        classeRepository.deleteById(id);
    }
}
