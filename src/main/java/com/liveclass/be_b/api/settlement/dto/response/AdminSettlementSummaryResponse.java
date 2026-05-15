package com.liveclass.be_b.api.settlement.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "운영자 예상 정산 집계 조회 응답")
public class AdminSettlementSummaryResponse {
    @ArraySchema(schema = @Schema(implementation = AdminSettlementSummaryItemResponse.class))
    private List<AdminSettlementSummaryItemResponse> items;
    @Schema(description = "전체 정산 예정 금액 합계", example = "270000")
    private long totalExpectedPayoutAmount;

    public static AdminSettlementSummaryResponse of(List<AdminSettlementSummaryItemResponse> items, long totalExpectedPayoutAmount) {
        return AdminSettlementSummaryResponse.builder()
                .items(items)
                .totalExpectedPayoutAmount(totalExpectedPayoutAmount)
                .build();
    }
}
