package com.liveclass.be_b.service.feepolicy;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.feepolicy.entity.FeePolicyHistory;
import com.liveclass.be_b.repository.feepolicy.FeePolicyHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeePolicyHistoryService {
    private static final String FEE_POLICY_HISTORY_ID_PREFIX = "fee-policy-history";

    private final FeePolicyHistoryRepository feePolicyHistoryRepository;

    @Transactional
    public FeePolicyHistory registerFeePolicyHistory(LocalDateTime effectiveStartedAt, int feeRatePercent) {
        if (feePolicyHistoryRepository.existsByEffectiveStartedAt(effectiveStartedAt)) {
            throw new BusinessException(ErrorCode.DUPLICATE_FEE_POLICY_HISTORY);
        }

        String uuid = UUID.randomUUID().toString();
        String feePolicyHistoryId = String.format("%s-%s", FEE_POLICY_HISTORY_ID_PREFIX, uuid);

        try {
            FeePolicyHistory feePolicyHistory = FeePolicyHistory.create(
                    feePolicyHistoryId,
                    feeRatePercent,
                    effectiveStartedAt
            );

            return feePolicyHistoryRepository.save(feePolicyHistory);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.DUPLICATE_FEE_POLICY_HISTORY);
        }
    }

    @Transactional(readOnly = true)
    public int findFeeRatePercentByAppliedAt(LocalDateTime appliedAt) {
        return feePolicyHistoryRepository.findFirstByEffectiveStartedAtLessThanEqualOrderByEffectiveStartedAtDesc(appliedAt)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_FEE_POLICY))
                .getFeeRatePercent();
    }

    @Transactional(readOnly = true)
    public List<FeePolicyHistory> findAllFeePolicyHistories() {
        return feePolicyHistoryRepository.findAllByOrderByEffectiveStartedAtAsc();
    }
}
