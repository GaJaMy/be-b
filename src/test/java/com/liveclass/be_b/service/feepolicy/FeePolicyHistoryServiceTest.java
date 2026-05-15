package com.liveclass.be_b.service.feepolicy;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.feepolicy.entity.FeePolicyHistory;
import com.liveclass.be_b.repository.feepolicy.FeePolicyHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeePolicyHistoryServiceTest {

    @Mock
    private FeePolicyHistoryRepository feePolicyHistoryRepository;

    @InjectMocks
    private FeePolicyHistoryService feePolicyHistoryService;

    @Test
    @DisplayName("수수료율 이력을 등록할 수 있다")
    void registerFeePolicyHistory() {
        LocalDateTime effectiveStartedAt = LocalDateTime.of(2025, 5, 10, 0, 0);
        when(feePolicyHistoryRepository.existsByEffectiveStartedAt(effectiveStartedAt)).thenReturn(false);
        when(feePolicyHistoryRepository.save(org.mockito.ArgumentMatchers.any(FeePolicyHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FeePolicyHistory result = feePolicyHistoryService.registerFeePolicyHistory(effectiveStartedAt, 30);

        assertThat(result.getId()).startsWith("fee-policy-history-");
        assertThat(result.getFeeRatePercent()).isEqualTo(30);
        assertThat(result.getEffectiveStartedAt()).isEqualTo(effectiveStartedAt);
    }

    @Test
    @DisplayName("동일한 적용 시각의 이력이 있으면 예외가 발생한다")
    void registerFeePolicyHistoryDuplicate() {
        LocalDateTime effectiveStartedAt = LocalDateTime.of(2025, 5, 10, 0, 0);
        when(feePolicyHistoryRepository.existsByEffectiveStartedAt(effectiveStartedAt)).thenReturn(true);

        assertThatThrownBy(() -> feePolicyHistoryService.registerFeePolicyHistory(effectiveStartedAt, 30))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_FEE_POLICY_HISTORY);
    }

    @Test
    @DisplayName("수수료율 이력 저장 시 DB 유니크 충돌이 나면 중복 이력 예외로 변환한다")
    void registerFeePolicyHistoryDuplicateByDataIntegrityViolation() {
        LocalDateTime effectiveStartedAt = LocalDateTime.of(2025, 5, 10, 0, 0);
        when(feePolicyHistoryRepository.existsByEffectiveStartedAt(effectiveStartedAt)).thenReturn(false);
        when(feePolicyHistoryRepository.save(org.mockito.ArgumentMatchers.any(FeePolicyHistory.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> feePolicyHistoryService.registerFeePolicyHistory(effectiveStartedAt, 30))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_FEE_POLICY_HISTORY);
    }

    @Test
    @DisplayName("수수료율 이력 목록을 조회할 수 있다")
    void findAllFeePolicyHistories() {
        List<FeePolicyHistory> histories = List.of(
                FeePolicyHistory.create("fee-policy-history-1", 20, LocalDateTime.of(2025, 4, 1, 0, 0)),
                FeePolicyHistory.create("fee-policy-history-2", 25, LocalDateTime.of(2025, 4, 15, 0, 0))
        );
        when(feePolicyHistoryRepository.findAllByOrderByEffectiveStartedAtAsc()).thenReturn(histories);

        List<FeePolicyHistory> result = feePolicyHistoryService.findAllFeePolicyHistories();

        assertThat(result).containsExactlyElementsOf(histories);
    }

    @Test
    @DisplayName("적용 시각 기준으로 가장 최근 수수료율을 조회할 수 있다")
    void findFeeRatePercentByAppliedAt() {
        LocalDateTime appliedAt = LocalDateTime.of(2025, 5, 10, 0, 0);
        FeePolicyHistory history = FeePolicyHistory.create(
                "fee-policy-history-1",
                30,
                LocalDateTime.of(2025, 5, 1, 0, 0)
        );
        when(feePolicyHistoryRepository.findFirstByEffectiveStartedAtLessThanEqualOrderByEffectiveStartedAtDesc(appliedAt))
                .thenReturn(Optional.of(history));

        int feeRatePercent = feePolicyHistoryService.findFeeRatePercentByAppliedAt(appliedAt);

        assertThat(feeRatePercent).isEqualTo(30);
    }

    @Test
    @DisplayName("적용 시각 이전의 수수료율 이력이 없으면 예외가 발생한다")
    void findFeeRatePercentByAppliedAtNotFound() {
        LocalDateTime appliedAt = LocalDateTime.of(2025, 5, 10, 0, 0);
        when(feePolicyHistoryRepository.findFirstByEffectiveStartedAtLessThanEqualOrderByEffectiveStartedAtDesc(appliedAt))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> feePolicyHistoryService.findFeeRatePercentByAppliedAt(appliedAt))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_FEE_POLICY);
    }

}
