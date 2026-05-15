package com.liveclass.be_b.api.sales.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "판매 내역 조회 응답")
public class SaleQueryResponse {
    @Schema(description = "판매 ID", example = "sale-1")
    private String saleId;
    @Schema(description = "강의 ID", example = "course-1")
    private String courseId;
    @Schema(description = "수강생 ID", example = "student-1")
    private String studentId;
    @Schema(description = "결제 금액", example = "50000")
    private Long amount;
    @Schema(description = "판매 당시 적용된 수수료율", example = "20")
    private Integer feeRatePercent;
    @Schema(description = "결제 일시(KST LocalDateTime)", example = "2025-03-05T10:00:00")
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
