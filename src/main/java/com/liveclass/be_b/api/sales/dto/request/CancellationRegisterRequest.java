package com.liveclass.be_b.api.sales.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancellationRegisterRequest {
    private Long refundAmount;
    private OffsetDateTime canceledAt;
}
