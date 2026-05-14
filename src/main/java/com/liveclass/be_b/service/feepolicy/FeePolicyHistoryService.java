package com.liveclass.be_b.service.feepolicy;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.feepolicy.entity.FeePolicyHistory;
import com.liveclass.be_b.repository.feepolicy.FeePolicyHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

        long count = feePolicyHistoryRepository.count();
        String feePolicyHistoryId = String.format("%s-%d", FEE_POLICY_HISTORY_ID_PREFIX, count);

        FeePolicyHistory feePolicyHistory = FeePolicyHistory.create(
                feePolicyHistoryId,
                feeRatePercent,
                effectiveStartedAt
        );

        return feePolicyHistoryRepository.save(feePolicyHistory);
    }

    @Transactional(readOnly = true)
    public List<FeePolicyHistory> findAllFeePolicyHistories() {
        return feePolicyHistoryRepository.findAllByOrderByEffectiveStartedAtAsc();
    }
}
