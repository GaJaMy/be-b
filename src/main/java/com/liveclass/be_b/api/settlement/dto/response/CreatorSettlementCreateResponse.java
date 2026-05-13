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
public class CreatorSettlementCreateResponse {
    private String settlementId;
    private SettlementStatus status;

    public static CreatorSettlementCreateResponse of(String settlementId, SettlementStatus status) {
        return CreatorSettlementCreateResponse.builder()
                .settlementId(settlementId)
                .status(status)
                .build();
    }
}
