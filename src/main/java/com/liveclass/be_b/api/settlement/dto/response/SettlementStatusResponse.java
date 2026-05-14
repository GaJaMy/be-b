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
@Schema(description = "정산 상태 변경 응답")
public class SettlementStatusResponse {
    @Schema(description = "정산 ID", example = "settlement-1")
    private String settlementId;
    @Schema(description = "변경된 정산 상태", example = "PAID")
    private SettlementStatus status;

    public static SettlementStatusResponse of(String settlementId, SettlementStatus status) {
        return SettlementStatusResponse.builder()
                .settlementId(settlementId)
                .status(status)
                .build();
    }
}
