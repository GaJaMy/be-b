package com.liveclass.be_b.service.feepolicy;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.feepolicy.entity.FeePolicy;
import com.liveclass.be_b.repository.feepolicy.FeePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeePolicyService {
    private final FeePolicyRepository feePolicyRepository;

    @Transactional(readOnly = true)
    public int getCurrentFeePolicyRatePercent() {
        return feePolicyRepository.findFirstByOrderByCreatedAtAsc()
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_FEE_POLICY))
                .getFeeRatePercent();
    }

    @Transactional
    public int updateCurrentFeePolicyRatePercent(int feePolicyPercent) {
        FeePolicy feePolicy = feePolicyRepository.findFirstByOrderByCreatedAtAsc()
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_FEE_POLICY));

        feePolicy.updateFeeRatePercent(feePolicyPercent);

        FeePolicy save = feePolicyRepository.save(feePolicy);

        return save.getFeeRatePercent();
    }
}
