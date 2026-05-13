package com.liveclass.be_b.service.feepolicy;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.feepolicy.entity.FeePolicyHistory;
import com.liveclass.be_b.repository.feepolicy.FeePolicyHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeePolicyHistoryService {
    private static final String FEE_POLICY_HISTORY_ID_PREFIX = "fee-policy-history";

    private final FeePolicyHistoryRepository feePolicyHistoryRepository;

    @Transactional
    public FeePolicyHistory registerFeePolicyHistory(YearMonth yearMonth, int feeRatePercent) {
        long count = feePolicyHistoryRepository.count();
        String feePolicyHistoryId = String.format("%s-%d", FEE_POLICY_HISTORY_ID_PREFIX, count);

        FeePolicyHistory feePolicyHistory = FeePolicyHistory.create(
                feePolicyHistoryId,
                feeRatePercent,
                yearMonth.toString()
        );

        return feePolicyHistoryRepository.save(feePolicyHistory);
    }

    @Transactional(readOnly = true)
    public List<FeePolicyHistory> findAllFeePolicyHistories() {
        return feePolicyHistoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public FeePolicyHistory findFeePolicyHistory(YearMonth yearMonth) {
        return feePolicyHistoryRepository.findByTargetYearMonth(yearMonth.toString())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_FEE_POLICY));
    }

    @Transactional(readOnly = true)
    public Map<YearMonth, FeePolicyHistory> findFeePolicyHistoryMap(YearMonth from, YearMonth to) {
        return feePolicyHistoryRepository.findByTargetYearMonthBetween(from.toString(), to.toString()).stream()
                .collect(Collectors.toMap(
                        feePolicyHistory -> YearMonth.parse(feePolicyHistory.getTargetYearMonth()),
                        feePolicyHistory -> feePolicyHistory
                ));
    }
}
