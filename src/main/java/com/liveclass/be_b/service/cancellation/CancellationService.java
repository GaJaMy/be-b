package com.liveclass.be_b.service.cancellation;

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
    private final static String CANCELLATION_ID_PREFIX = "cancel";
    private final CancellationRepository cancellationRepository;

    @Transactional
    public String registerCancellation(SaleRecord saleRecord, Long refundAmount, LocalDateTime canceledAt) {
        long count = cancellationRepository.count();
        String cancelId = String.format("%s-%d", CANCELLATION_ID_PREFIX, count);

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
