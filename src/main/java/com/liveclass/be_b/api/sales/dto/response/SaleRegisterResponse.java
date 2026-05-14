package com.liveclass.be_b.api.sales.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "판매 내역 등록 응답")
public class SaleRegisterResponse {
    @Schema(description = "등록된 판매 ID", example = "sale-1")
    private String saleId;

    public static SaleRegisterResponse of(String saleId) {
        return SaleRegisterResponse.builder()
                .saleId(saleId)
                .build();
    }
}
