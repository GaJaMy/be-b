package com.liveclass.be_b.support;

import com.liveclass.be_b.api.settlement.dto.internal.SettlementMetrics;
import com.liveclass.be_b.domain.admin.entity.Admin;
import com.liveclass.be_b.domain.cancellation.entity.CancellationRecord;
import com.liveclass.be_b.domain.course.entity.Course;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.domain.feepolicy.entity.FeePolicy;
import com.liveclass.be_b.domain.feepolicy.entity.FeePolicyHistory;
import com.liveclass.be_b.domain.sale.entity.SaleRecord;
import com.liveclass.be_b.domain.settlement.entity.Settlement;
import com.liveclass.be_b.security.AuthenticatedPrincipal;

import java.time.LocalDateTime;

public final class TestFixtureFactory {

    private TestFixtureFactory() {
    }

    public static Creator creator() {
        return Creator.create("creator-1", "creator-1", "encoded-password", "김강사");
    }

    public static Creator creator(String id, String loginId, String name) {
        return Creator.create(id, loginId, "encoded-password", name);
    }

    public static Admin admin() {
        return Admin.create("admin-1", "admin-1", "encoded-password", "서관리자");
    }

    public static Course course(Creator creator) {
        return Course.create("course-1", creator, "Spring Boot 입문", LocalDateTime.of(2025, 3, 1, 0, 0));
    }

    public static Course course(String id, Creator creator, String title) {
        return Course.create(id, creator, title, LocalDateTime.of(2025, 3, 1, 0, 0));
    }

    public static SaleRecord saleRecord(Course course, Creator creator) {
        return SaleRecord.create("sale-1", course, creator, "student-1", 50000L, 20, LocalDateTime.of(2025, 3, 5, 10, 0));
    }

    public static SaleRecord saleRecord(
            String id,
            Course course,
            Creator creator,
            String studentId,
            long amount,
            int feeRatePercent,
            LocalDateTime paidAt
    ) {
        return SaleRecord.create(id, course, creator, studentId, amount, feeRatePercent, paidAt);
    }

    public static CancellationRecord cancellationRecord(SaleRecord saleRecord, long refundAmount, LocalDateTime canceledAt) {
        return CancellationRecord.create("cancel-1", saleRecord, refundAmount, canceledAt);
    }

    public static CancellationRecord cancellationRecord(
            String id,
            SaleRecord saleRecord,
            long refundAmount,
            LocalDateTime canceledAt
    ) {
        return CancellationRecord.create(id, saleRecord, refundAmount, canceledAt);
    }

    public static FeePolicy feePolicy(int feeRatePercent) {
        return FeePolicy.create("fee-policy-1", feeRatePercent);
    }

    public static FeePolicyHistory feePolicyHistory(LocalDateTime effectiveStartedAt, int feeRatePercent) {
        return FeePolicyHistory.create("fee-policy-history-1", feeRatePercent, effectiveStartedAt);
    }

    public static SettlementMetrics settlementMetrics() {
        return new SettlementMetrics(260000L, 110000L, 150000L, 30000L, 120000L, 4, 2);
    }

    public static Settlement settlement(Creator creator) {
        return Settlement.create(
                "settlement-1",
                creator,
                "2025-03",
                260000L,
                110000L,
                150000L,
                30000L,
                120000L,
                4,
                2,
                LocalDateTime.of(2025, 4, 1, 9, 0)
        );
    }

    public static AuthenticatedPrincipal adminPrincipal() {
        return new AuthenticatedPrincipal(
                "admin-1",
                "encoded-password",
                "admin-1",
                AuthenticatedPrincipal.ADMIN_ROLE,
                AuthenticatedPrincipal.ADMIN_PRINCIPAL_TYPE
        );
    }

    public static AuthenticatedPrincipal creatorPrincipal() {
        return new AuthenticatedPrincipal(
                "creator-1",
                "encoded-password",
                "creator-1",
                AuthenticatedPrincipal.CREATOR_ROLE,
                AuthenticatedPrincipal.CREATOR_PRINCIPAL_TYPE
        );
    }
}
