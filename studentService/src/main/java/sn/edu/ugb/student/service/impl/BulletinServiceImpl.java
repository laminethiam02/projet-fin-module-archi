package sn.edu.ugb.student.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.student.domain.Bulletin;
import sn.edu.ugb.student.repository.BulletinRepository;
import sn.edu.ugb.student.service.BulletinService;
import sn.edu.ugb.student.service.dto.BulletinDTO;
import sn.edu.ugb.student.service.mapper.BulletinMapper;

/**
 * Service Implementation for managing {@link sn.edu.ugb.student.domain.Bulletin}.
 */
@Service
@Transactional
public class BulletinServiceImpl implements BulletinService {

    private static final Logger LOG = LoggerFactory.getLogger(BulletinServiceImpl.class);

    private final BulletinRepository bulletinRepository;

    private final BulletinMapper bulletinMapper;

    public BulletinServiceImpl(BulletinRepository bulletinRepository, BulletinMapper bulletinMapper) {
        this.bulletinRepository = bulletinRepository;
        this.bulletinMapper = bulletinMapper;
    }

    @Override
    public BulletinDTO save(BulletinDTO bulletinDTO) {
        LOG.debug("Request to save Bulletin : {}", bulletinDTO);
        Bulletin bulletin = bulletinMapper.toEntity(bulletinDTO);
        bulletin = bulletinRepository.save(bulletin);
        return bulletinMapper.toDto(bulletin);
    }

    @Override
    public BulletinDTO update(BulletinDTO bulletinDTO) {
        LOG.debug("Request to update Bulletin : {}", bulletinDTO);
        Bulletin bulletin = bulletinMapper.toEntity(bulletinDTO);
        bulletin = bulletinRepository.save(bulletin);
        return bulletinMapper.toDto(bulletin);
    }

    @Override
    public Optional<BulletinDTO> partialUpdate(BulletinDTO bulletinDTO) {
        LOG.debug("Request to partially update Bulletin : {}", bulletinDTO);

        return bulletinRepository
            .findById(bulletinDTO.getId())
            .map(existingBulletin -> {
                bulletinMapper.partialUpdate(existingBulletin, bulletinDTO);

                return existingBulletin;
            })
            .map(bulletinRepository::save)
            .map(bulletinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BulletinDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Bulletins");
        return bulletinRepository.findAll(pageable).map(bulletinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BulletinDTO> findOne(Long id) {
        LOG.debug("Request to get Bulletin : {}", id);
        return bulletinRepository.findById(id).map(bulletinMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Bulletin : {}", id);
        bulletinRepository.deleteById(id);
    }
}
