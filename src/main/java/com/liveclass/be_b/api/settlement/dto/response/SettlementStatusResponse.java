package com.liveclass.be_b.api.settlement.dto.response;

import com.liveclass.be_b.domain.settlement.enums.SettlementStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementStatusResponse {
    private String settlementId;
    private SettlementStatus status;

    public static SettlementStatusResponse of(String settlementId, SettlementStatus status) {
        return SettlementStatusResponse.builder()
                .settlementId(settlementId)
                .status(status)
                .build();
    }
}
