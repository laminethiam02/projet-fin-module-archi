package sn.edu.ugb.teacher.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.teacher.domain.ChargeHoraire;
import sn.edu.ugb.teacher.repository.ChargeHoraireRepository;
import sn.edu.ugb.teacher.service.ChargeHoraireService;
import sn.edu.ugb.teacher.service.dto.ChargeHoraireDTO;
import sn.edu.ugb.teacher.service.mapper.ChargeHoraireMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.teacher.domain.ChargeHoraire}.
 */
@Service
@Transactional
public class ChargeHoraireServiceImpl implements ChargeHoraireService {

    private static final Logger LOG = LoggerFactory.getLogger(ChargeHoraireServiceImpl.class);

    private final ChargeHoraireRepository chargeHoraireRepository;

    private final ChargeHoraireMapper chargeHoraireMapper;

    public ChargeHoraireServiceImpl(ChargeHoraireRepository chargeHoraireRepository, ChargeHoraireMapper chargeHoraireMapper) {
        this.chargeHoraireRepository = chargeHoraireRepository;
        this.chargeHoraireMapper = chargeHoraireMapper;
    }

    @Override
    public ChargeHoraireDTO save(ChargeHoraireDTO chargeHoraireDTO) {
        LOG.debug("Request to save ChargeHoraire : {}", chargeHoraireDTO);
        ChargeHoraire chargeHoraire = chargeHoraireMapper.toEntity(chargeHoraireDTO);
        chargeHoraire = chargeHoraireRepository.save(chargeHoraire);
        return chargeHoraireMapper.toDto(chargeHoraire);
    }

    @Override
    public ChargeHoraireDTO update(ChargeHoraireDTO chargeHoraireDTO) {
        LOG.debug("Request to update ChargeHoraire : {}", chargeHoraireDTO);
        ChargeHoraire chargeHoraire = chargeHoraireMapper.toEntity(chargeHoraireDTO);
        chargeHoraire = chargeHoraireRepository.save(chargeHoraire);
        return chargeHoraireMapper.toDto(chargeHoraire);
    }

    @Override
    public Optional<ChargeHoraireDTO> partialUpdate(ChargeHoraireDTO chargeHoraireDTO) {
        LOG.debug("Request to partially update ChargeHoraire : {}", chargeHoraireDTO);

        return chargeHoraireRepository
            .findById(chargeHoraireDTO.getId())
            .map(existingChargeHoraire -> {
                chargeHoraireMapper.partialUpdate(existingChargeHoraire, chargeHoraireDTO);

                return existingChargeHoraire;
            })
            .map(chargeHoraireRepository::save)
            .map(chargeHoraireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChargeHoraireDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ChargeHoraires");
        return chargeHoraireRepository.findAll(pageable).map(chargeHoraireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChargeHoraireDTO> findOne(Long id) {
        LOG.debug("Request to get ChargeHoraire : {}", id);
        return chargeHoraireRepository.findById(id).map(chargeHoraireMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ChargeHoraire : {}", id);
        chargeHoraireRepository.deleteById(id);
    }
}
