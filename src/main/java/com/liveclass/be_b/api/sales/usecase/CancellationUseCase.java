package com.liveclass.be_b.api.sales.usecase;

import com.liveclass.be_b.api.sales.dto.request.CancellationRegisterRequest;
import com.liveclass.be_b.api.sales.dto.response.CancellationRegisterResponse;
import com.liveclass.be_b.common.util.DateTimeUtil;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.service.cancellation.CancellationService;
import com.liveclass.be_b.service.sale.SaleRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CancellationUseCase {
    private final CancellationService cancellationService;
    private final SaleRecordService saleRecordService;

    @Transactional
    public CancellationRegisterResponse registerCancellation(String saleId, CancellationRegisterRequest request) {
        SaleRecord saleRecord = saleRecordService.findSaleRecord(saleId);

        String cancellationId = cancellationService.registerCancellation(
                saleRecord,
                request.getRefundAmount(),
                DateTimeUtil.toKstLocalDateTime(request.getCanceledAt())
        );

        return CancellationRegisterResponse.of(cancellationId);
    }
}
