package com.liveclass.be_b.api.sales.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "판매 내역 등록 요청")
public class SaleRegisterRequest {
    @Schema(description = "판매 ID", example = "sale-1")
    @NotBlank(message = "판매 ID는 필수입니다.")
    private String saleId;

    @Schema(description = "강의 ID", example = "course-1")
    @NotBlank(message = "강의 ID는 필수입니다.")
    private String courseId;

    @Schema(description = "수강생 ID", example = "student-1")
    @NotBlank(message = "수강생 ID는 필수입니다.")
    private String studentId;

    @Schema(description = "결제 금액", example = "50000", minimum = "0")
    @NotNull(message = "결제 금액은 필수입니다.")
    @Min(value = 0, message = "결제 금액은 0 이상이어야 합니다.")
    private Long amount;

    @Schema(description = "결제 일시", example = "2025-03-05T10:00:00+09:00", type = "string", format = "date-time")
    @NotNull(message = "결제 일시는 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime paidAt;
}
