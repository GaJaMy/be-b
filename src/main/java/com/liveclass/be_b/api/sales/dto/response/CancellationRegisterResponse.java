package com.liveclass.be_b.api.sales.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "취소 내역 등록 응답")
public class CancellationRegisterResponse {
    @Schema(description = "등록된 취소 ID", example = "cancel-1")
    private String cancelId;

    public static CancellationRegisterResponse of(String cancelId) {
        return CancellationRegisterResponse.builder()
                .cancelId(cancelId)
                .build();
    }
}
