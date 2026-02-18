package invoice;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        DataRetriever retriever = new DataRetriever();

        List<InvoiceTotal> totals = retriever.findInvoiceTotals();
        totals.forEach(System.out::println);
    }
}