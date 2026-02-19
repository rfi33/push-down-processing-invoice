package invoice;

import java.math.BigDecimal;

public class InvoiceTotal {

    private final int invoiceId;
    private final String customerName;
    private final String status;
    private final BigDecimal total;

    public InvoiceTotal(int invoiceId, String customerName, BigDecimal total) {
        this.invoiceId    = invoiceId;
        this.customerName = customerName;
        this.status       = null;
        this.total        = total;
    }

    public InvoiceTotal(int invoiceId, String customerName, String status, BigDecimal total) {
        this.invoiceId    = invoiceId;
        this.customerName = customerName;
        this.status       = status;
        this.total        = total;
    }

    public int        getInvoiceId()    { return invoiceId;    }
    public String     getCustomerName() { return customerName; }
    public String     getStatus()       { return status;       }
    public BigDecimal getTotal()        { return total;        }

    @Override
    public String toString() {
        if (status == null) {
            return String.format("%d | %s | %.2f", invoiceId, customerName, total);
        }
        return String.format("%d | %s | %s | %.2f", invoiceId, customerName, status, total);
    }
}