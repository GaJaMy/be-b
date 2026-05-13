package com.liveclass.be_b.service.creator;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.repository.creator.CreatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatorService {
    private final CreatorRepository creatorRepository;

    @Transactional(readOnly = true)
    public Creator findCreator(String creatorId) {
        return creatorRepository.findById(creatorId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CREATOR));
    }

    @Transactional(readOnly = true)
    public List<Creator> findAllCreators() {
        return creatorRepository.findAll();
    }
}
