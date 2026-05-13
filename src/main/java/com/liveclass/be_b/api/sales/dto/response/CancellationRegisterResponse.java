package com.liveclass.be_b.api.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancellationRegisterResponse {
    private String cancelId;

    public static CancellationRegisterResponse of(String cancelId) {
        return CancellationRegisterResponse.builder()
                .cancelId(cancelId)
                .build();
    }
}
