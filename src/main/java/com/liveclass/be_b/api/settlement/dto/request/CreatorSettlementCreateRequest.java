package com.liveclass.be_b.api.settlement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CreatorSettlementCreateRequest {

    @NotNull(message = "정산 대상 연월은 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth yearMonth;
}
