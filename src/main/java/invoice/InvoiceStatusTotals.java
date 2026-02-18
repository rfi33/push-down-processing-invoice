package invoice;

import java.math.BigDecimal;

public class InvoiceStatusTotals {

    private final BigDecimal totalPaid;
    private final BigDecimal totalConfirmed;
    private final BigDecimal totalDraft;

    public InvoiceStatusTotals(BigDecimal totalPaid, BigDecimal totalConfirmed, BigDecimal totalDraft) {
        this.totalPaid      = totalPaid;
        this.totalConfirmed = totalConfirmed;
        this.totalDraft     = totalDraft;
    }

    public BigDecimal getTotalPaid()      { return totalPaid;      }
    public BigDecimal getTotalConfirmed() { return totalConfirmed; }
    public BigDecimal getTotalDraft()     { return totalDraft;     }

    @Override
    public String toString() {
        return String.format("total_paid = %.2f%ntotal_confirmed = %.2f%ntotal_draft = %.2f",
                totalPaid, totalConfirmed, totalDraft);
    }
}