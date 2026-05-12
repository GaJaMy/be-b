package com.liveclass.be_b.api.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleRegisterResponse {
    private String saleId;

    public static SaleRegisterResponse of(String saleId) {
        return SaleRegisterResponse.builder()
                .saleId(saleId)
                .build();
    }
}
