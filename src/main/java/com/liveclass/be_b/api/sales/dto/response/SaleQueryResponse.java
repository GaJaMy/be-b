package com.liveclass.be_b.api.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleQueryResponse {
    private String saleId;
    private String courseId;
    private String studentId;
    private Long amount;
    private Integer feeRatePercent;
    private LocalDateTime paidAt;

    public static SaleQueryResponse of(
            String saleId,
            String courseId,
            String studentId,
            Long amount,
            Integer feeRatePercent,
            LocalDateTime paidAt
    ) {
        return SaleQueryResponse.builder()
                .saleId(saleId)
                .courseId(courseId)
                .studentId(studentId)
                .amount(amount)
                .feeRatePercent(feeRatePercent)
                .paidAt(paidAt)
                .build();
    }
}
