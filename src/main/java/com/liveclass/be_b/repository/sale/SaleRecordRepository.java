package com.liveclass.be_b.repository.sale;

import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRecordRepository extends JpaRepository<SaleRecord, String> {
}
