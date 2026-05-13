package com.liveclass.be_b.api.sales.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Min(0)
    private Long amount;
    private OffsetDateTime paidAt;
}
