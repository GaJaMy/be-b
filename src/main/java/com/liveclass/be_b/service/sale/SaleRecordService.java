package com.liveclass.be_b.service.sale;

import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.repository.sale.SaleRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SaleRecordService {
    private final SaleRecordRepository saleRecordRepository;

    public String registerSale(Course course, String studentId, Long amount, LocalDateTime paidAt) {
        long count = saleRecordRepository.count();
        String saleId = String.format("sale-%d", count);

        SaleRecord saleRecord =
                SaleRecord.create(saleId, course, course.getCreator(), studentId, amount, paidAt);

        SaleRecord save = saleRecordRepository.save(saleRecord);

        return save.getId();
    }
}
