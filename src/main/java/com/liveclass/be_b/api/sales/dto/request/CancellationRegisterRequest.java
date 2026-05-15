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
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "취소 내역 등록 요청")
public class CancellationRegisterRequest {
    @Schema(description = "취소 ID", example = "cancel-1")
    @NotBlank(message = "취소 ID는 필수입니다.")
    private String cancelId;

    @Schema(description = "환불 금액", example = "30000", minimum = "0")
    @NotNull(message = "환불 금액은 필수입니다.")
    @Min(value = 0, message = "환불 금액은 0 이상이어야 합니다.")
    private Long refundAmount;

    @Schema(description = "취소 일시", example = "2025-03-28T15:00:00+09:00", type = "string", format = "date-time")
    @NotNull(message = "취소 일시는 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime canceledAt;
}
