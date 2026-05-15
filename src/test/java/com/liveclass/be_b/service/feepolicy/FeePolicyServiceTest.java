package com.liveclass.be_b.service.feepolicy;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.feepolicy.entity.FeePolicy;
import com.liveclass.be_b.repository.feepolicy.FeePolicyRepository;
import com.liveclass.be_b.support.TestFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeePolicyServiceTest {

    @Mock
    private FeePolicyRepository feePolicyRepository;

    @InjectMocks
    private FeePolicyService feePolicyService;

    @Test
    @DisplayName("현재 수수료율을 조회할 수 있다")
    void getCurrentFeePolicyRatePercent() {
        when(feePolicyRepository.findFirstByOrderByCreatedAtAsc())
                .thenReturn(Optional.of(TestFixtureFactory.feePolicy(20)));

        int result = feePolicyService.getCurrentFeePolicyRatePercent();

        assertThat(result).isEqualTo(20);
    }

    @Test
    @DisplayName("현재 수수료율이 없으면 예외가 발생한다")
    void getCurrentFeePolicyRatePercentNotFound() {
        when(feePolicyRepository.findFirstByOrderByCreatedAtAsc())
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> feePolicyService.getCurrentFeePolicyRatePercent())
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_FEE_POLICY);
    }

    @Test
    @DisplayName("현재 수수료율을 변경할 수 있다")
    void updateCurrentFeePolicyRatePercent() {
        FeePolicy feePolicy = TestFixtureFactory.feePolicy(20);
        when(feePolicyRepository.findFirstByOrderByCreatedAtAsc()).thenReturn(Optional.of(feePolicy));
        when(feePolicyRepository.save(feePolicy)).thenReturn(feePolicy);

        int result = feePolicyService.updateCurrentFeePolicyRatePercent(25);

        assertThat(result).isEqualTo(25);
        assertThat(feePolicy.getFeeRatePercent()).isEqualTo(25);
    }

}
