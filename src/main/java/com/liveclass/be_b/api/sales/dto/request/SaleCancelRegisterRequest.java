package com.liveclass.be_b.api.sales.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleCancelRegisterRequest {
    private Long refundAmount;
    private LocalDateTime canceledAt;
}
