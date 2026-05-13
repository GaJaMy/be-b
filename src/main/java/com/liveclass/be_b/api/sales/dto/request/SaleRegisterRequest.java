package com.liveclass.be_b.api.sales.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class SaleRegisterRequest {
    @NotBlank(message = "강의 ID는 필수입니다.")
    private String courseId;

    @NotBlank(message = "수강생 ID는 필수입니다.")
    private String studentId;

    @NotNull(message = "결제 금액은 필수입니다.")
    @Min(value = 0, message = "결제 금액은 0 이상이어야 합니다.")
    private Long amount;

    @NotNull(message = "결제 일시는 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime paidAt;
}
