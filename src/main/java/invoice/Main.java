package invoice;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        DataRetriever retriever = new DataRetriever();

        List<InvoiceTotal> totals = retriever.findInvoiceTotals();
        totals.forEach(System.out::println);

        List<InvoiceTotal> confirmedAndPaid = retriever.findConfirmedAndPaidInvoiceTotals();
        confirmedAndPaid.forEach(System.out::println);

        InvoiceStatusTotals statusTotals = retriever.computeStatusTotals();
        System.out.println(statusTotals);

        Double weightedTurnover = retriever.computeWeightedTurnover();
        System.out.printf("%.2f%n", weightedTurnover);
    }
}