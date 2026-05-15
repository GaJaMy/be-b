package com.liveclass.be_b.api.feepolicy.usecase;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyHistoryRegisterRequest;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyHistoryQueryResponse;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyHistoryRegisterResponse;
import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.feepolicy.entity.FeePolicyHistory;
import com.liveclass.be_b.service.feepolicy.FeePolicyHistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeePolicyHistoryUseCaseTest {

    @Mock
    private FeePolicyHistoryService feePolicyHistoryService;

    @InjectMocks
    private FeePolicyHistoryUseCase feePolicyHistoryUseCase;

    @Test
    @DisplayName("수수료율 이력 등록 요청을 응답으로 변환한다")
    void registerFeePolicyHistory() {
        OffsetDateTime effectiveStartedAt = OffsetDateTime.of(2025, 5, 10, 0, 0, 0, 0, ZoneOffset.ofHours(9));
        FeePolicyHistoryRegisterRequest request = FeePolicyHistoryRegisterRequest.builder()
                .effectiveStartedAt(effectiveStartedAt)
                .feeRatePercent(30)
                .build();
        FeePolicyHistory history = FeePolicyHistory.create("fee-policy-history-1", 30, LocalDateTime.of(2025, 5, 10, 0, 0));
        when(feePolicyHistoryService.registerFeePolicyHistory(eq(LocalDateTime.of(2025, 5, 10, 0, 0)), eq(30)))
                .thenReturn(history);

        FeePolicyHistoryRegisterResponse response = feePolicyHistoryUseCase.registerFeePolicyHistory(request);

        assertThat(response.getFeePolicyHistoryId()).isEqualTo("fee-policy-history-1");
        assertThat(response.getFeeRatePercent()).isEqualTo(30);
        assertThat(response.getEffectiveStartedAt()).isEqualTo(effectiveStartedAt);
    }

    @Test
    @DisplayName("수수료율 이력 목록을 응답으로 변환한다")
    void queryFeePolicyHistories() {
        FeePolicyHistory history = FeePolicyHistory.create("fee-policy-history-1", 30, LocalDateTime.of(2025, 5, 10, 0, 0));
        when(feePolicyHistoryService.findAllFeePolicyHistories()).thenReturn(List.of(history));

        List<FeePolicyHistoryQueryResponse> responses = feePolicyHistoryUseCase.queryFeePolicyHistories();

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().getFeePolicyHistoryId()).isEqualTo("fee-policy-history-1");
        assertThat(responses.getFirst().getFeeRatePercent()).isEqualTo(30);
    }

    @Test
    @DisplayName("수수료율 이력 등록 시 중복 이력이 있으면 예외가 발생한다")
    void registerFeePolicyHistoryDuplicate() {
        OffsetDateTime effectiveStartedAt = OffsetDateTime.of(2025, 5, 10, 0, 0, 0, 0, ZoneOffset.ofHours(9));
        FeePolicyHistoryRegisterRequest request = FeePolicyHistoryRegisterRequest.builder()
                .effectiveStartedAt(effectiveStartedAt)
                .feeRatePercent(30)
                .build();
        when(feePolicyHistoryService.registerFeePolicyHistory(eq(LocalDateTime.of(2025, 5, 10, 0, 0)), eq(30)))
                .thenThrow(new BusinessException(ErrorCode.DUPLICATE_FEE_POLICY_HISTORY));

        assertThatThrownBy(() -> feePolicyHistoryUseCase.registerFeePolicyHistory(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_FEE_POLICY_HISTORY);
    }
}
