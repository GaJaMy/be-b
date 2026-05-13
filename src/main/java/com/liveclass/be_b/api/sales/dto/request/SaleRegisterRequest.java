package com.liveclass.be_b.api.sales.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleRegisterRequest {
    private String courseId;
    private String studentId;
    private Long amount;
    private OffsetDateTime paidAt;
}
