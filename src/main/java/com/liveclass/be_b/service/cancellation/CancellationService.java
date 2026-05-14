package com.liveclass.be_b.service.cancellation;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.cancellation.entity.CancellationRecord;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.repository.cancellation.CancellationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CancellationService {
    private final CancellationRepository cancellationRepository;

    @Transactional
    public String registerCancellation(String cancelId, SaleRecord saleRecord, Long refundAmount, LocalDateTime canceledAt) {
        if (cancellationRepository.existsById(cancelId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_CANCELLATION);
        }

        if (canceledAt.isBefore(saleRecord.getPaidAt())) {
            throw new BusinessException(ErrorCode.INVALID_CANCELLATION_DATE);
        }

        Long totalRefundAmount = cancellationRepository.sumRefundAmountBySaleRecord(saleRecord);
        if (totalRefundAmount + refundAmount > saleRecord.getAmount()) {
            throw new BusinessException(ErrorCode.OVER_SALE_AMOUNT);
        }
        
        CancellationRecord cancellationRecord =
                CancellationRecord.create(cancelId, saleRecord, refundAmount, canceledAt);

        CancellationRecord save = cancellationRepository.save(cancellationRecord);

        return save.getId();
    }

    @Transactional(readOnly = true)
    public List<CancellationRecord> queryCancellationBetweenFromTo(Creator creator, LocalDateTime from, LocalDateTime to) {
        return cancellationRepository.findByCreatorAndCanceledAtGreaterThanEqualAndCanceledAtLessThan(
                creator,
                from,
                to
        );
    }

    @Transactional(readOnly = true)
    public List<CancellationRecord> queryCancellationBetweenFromTo(List<Creator> creatorList, LocalDateTime from, LocalDateTime to) {
        return cancellationRepository.findByCreatorInAndCanceledAtGreaterThanEqualAndCanceledAtLessThan(
                creatorList,
                from,
                to
        );
    }
}
