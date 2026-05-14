package com.liveclass.be_b.api.settlement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "크리에이터 실제 정산 생성 요청")
public class CreatorSettlementCreateRequest {

    @Schema(description = "정산 대상 연월", example = "2025-03", type = "string", pattern = "yyyy-MM")
    @NotNull(message = "정산 대상 연월은 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth yearMonth;
}
