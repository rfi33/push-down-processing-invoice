package invoice;

import java.math.BigDecimal;

public class InvoiceTaxSummary {

    private final int        invoiceId;
    private final BigDecimal totalHt;
    private final BigDecimal totalTva;
    private final BigDecimal totalTtc;

    public InvoiceTaxSummary(int invoiceId, BigDecimal totalHt, BigDecimal totalTva, BigDecimal totalTtc) {
        this.invoiceId = invoiceId;
        this.totalHt   = totalHt;
        this.totalTva  = totalTva;
        this.totalTtc  = totalTtc;
    }

    public int        getInvoiceId() { return invoiceId; }
    public BigDecimal getTotalHt()   { return totalHt;   }
    public BigDecimal getTotalTva()  { return totalTva;  }
    public BigDecimal getTotalTtc()  { return totalTtc;  }

    @Override
    public String toString() {
        return String.format("%d | HT %.2f | TVA %.2f | TTC %.2f",
                invoiceId, totalHt, totalTva, totalTtc);
    }
}