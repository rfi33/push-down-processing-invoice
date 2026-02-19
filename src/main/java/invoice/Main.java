package invoice;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        DataRetriever retriever = new DataRetriever();

        List<InvoiceTotal> totals = retriever.findInvoiceTotals();
        totals.forEach(System.out::println);
        System.out.println();

        List<InvoiceTotal> confirmedAndPaid = retriever.findConfirmedAndPaidInvoiceTotals();
        confirmedAndPaid.forEach(System.out::println);
        System.out.println();

        InvoiceStatusTotals statusTotals = retriever.computeStatusTotals();
        System.out.println(statusTotals);
        System.out.println();

        Double weightedTurnover = retriever.computeWeightedTurnover();
        System.out.printf("%.2f%n", weightedTurnover);
        System.out.println();

        List<InvoiceTaxSummary> taxSummaries = retriever.findInvoiceTaxSummaries();
        taxSummaries.forEach(System.out::println);
        System.out.println();

        BigDecimal weightedTurnoverTtc = retriever.computeWeightedTurnoverTtc();
        System.out.println(weightedTurnoverTtc);
    }
}