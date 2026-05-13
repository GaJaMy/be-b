package com.liveclass.be_b.service.sale;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.repository.sale.SaleRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleRecordService {
    private final static String SALE_ID_PREFIX = "sale";

    private final SaleRecordRepository saleRecordRepository;

    @Transactional
    public String registerSale(
            Course course,
            String studentId,
            Long amount,
            Integer feeRatePercent,
            LocalDateTime paidAt
    ) {
        long count = saleRecordRepository.count();

        String saleId = String.format("%s-%d", SALE_ID_PREFIX, count);

        SaleRecord saleRecord =
                SaleRecord.create(saleId, course, course.getCreator(), studentId, amount, feeRatePercent, paidAt);

        SaleRecord save = saleRecordRepository.save(saleRecord);

        return save.getId();
    }

    @Transactional(readOnly = true)
    public List<SaleRecord> querySaleBetweenFromTo(Creator creator, LocalDateTime from, LocalDateTime to) {
        return saleRecordRepository.findByCreatorAndPaidAtBetweenWithCreator(creator, from, to);
    }

    @Transactional(readOnly = true)
    public SaleRecord findSaleRecord(String saleId) {
        return saleRecordRepository.findByIdWithCreator(saleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SALE));
    }

    @Transactional(readOnly = true)
    public List<SaleRecord> querySaleBetweenFromTo(List<Creator> creatorList, LocalDateTime from, LocalDateTime to) {
        return saleRecordRepository.findByCreatorAndPaidAtBetweenInCreator(creatorList, from, to);
    }
}
