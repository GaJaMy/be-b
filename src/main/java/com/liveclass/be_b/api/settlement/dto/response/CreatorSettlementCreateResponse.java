package com.liveclass.be_b.api.settlement.dto.response;

import com.liveclass.be_b.domain.settlement.enums.SettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "크리에이터 실제 정산 생성 응답")
public class CreatorSettlementCreateResponse {
    @Schema(description = "생성된 정산 ID", example = "settlement-1")
    private String settlementId;
    @Schema(description = "생성 직후 정산 상태", example = "PENDING")
    private SettlementStatus status;

    public static CreatorSettlementCreateResponse of(String settlementId, SettlementStatus status) {
        return CreatorSettlementCreateResponse.builder()
                .settlementId(settlementId)
                .status(status)
                .build();
    }
}
