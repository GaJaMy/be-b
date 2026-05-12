package com.liveclass.be_b.api.sales.usecase;

import com.liveclass.be_b.service.cancellation.CancellationService;
import com.liveclass.be_b.service.sale.SaleRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancellationUseCase {
    private final CancellationService cancellationService;
    private final SaleRecordService saleRecordService;
}
