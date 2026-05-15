package com.liveclass.be_b.api.feepolicy.usecase;

import com.liveclass.be_b.api.feepolicy.dto.request.FeePolicyUpdateRequest;
import com.liveclass.be_b.api.feepolicy.dto.response.FeePolicyUpdateResponse;
import com.liveclass.be_b.service.feepolicy.FeePolicyHistoryService;
import com.liveclass.be_b.service.feepolicy.FeePolicyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeePolicyUseCaseTest {

    @Mock
    private FeePolicyService feePolicyService;

    @Mock
    private FeePolicyHistoryService feePolicyHistoryService;

    @InjectMocks
    private FeePolicyUseCase feePolicyUseCase;

    @Test
    @DisplayName("현재 수수료율 변경과 이력 등록을 함께 수행한다")
    void updateCurrentFeePolicy() {
        FeePolicyUpdateRequest request = FeePolicyUpdateRequest.builder()
                .feeRatePercent(25)
                .build();
        when(feePolicyService.updateCurrentFeePolicyRatePercent(25)).thenReturn(25);

        FeePolicyUpdateResponse response = feePolicyUseCase.updateCurrentFeePolicy(request);

        assertThat(response.getFeeRatePercent()).isEqualTo(25);
        verify(feePolicyHistoryService).registerFeePolicyHistory(any(LocalDateTime.class), org.mockito.ArgumentMatchers.eq(25));
    }
}
