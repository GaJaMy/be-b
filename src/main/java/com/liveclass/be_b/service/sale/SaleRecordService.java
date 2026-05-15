package com.liveclass.be_b.service.sale;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.repository.sale.SaleRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleRecordService {
    private final SaleRecordRepository saleRecordRepository;

    @Transactional
    public String registerSale(
            Course course,
            String saleId,
            String studentId,
            Long amount,
            Integer feeRatePercent,
            LocalDateTime paidAt
    ) {
        if (saleRecordRepository.existsById(saleId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_SALE);
        }

        try {
            SaleRecord saleRecord =
                    SaleRecord.create(saleId, course, course.getCreator(), studentId, amount, feeRatePercent, paidAt);

            SaleRecord save = saleRecordRepository.save(saleRecord);

            return save.getId();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.DUPLICATE_SALE);
        }
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

    @Transactional
    public SaleRecord findSaleRecordWithLock(String saleId) {
        return saleRecordRepository.findByIdWithCreatorWithLock(saleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SALE));
    }

    @Transactional(readOnly = true)
    public List<SaleRecord> querySaleBetweenFromTo(List<Creator> creatorList, LocalDateTime from, LocalDateTime to) {
        return saleRecordRepository.findByCreatorAndPaidAtBetweenInCreator(creatorList, from, to);
    }
}
