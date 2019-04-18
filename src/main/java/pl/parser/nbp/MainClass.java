package pl.parser.nbp;

import org.jetbrains.annotations.NotNull;

public class MainClass {

    public static void main(@NotNull String[] args) throws Exception {
        final String currency = args[0];
        final String dateFrom = args[1];
        final String dateTo = args[2];

        ParseXMLFromNBP xml = new ParseXMLFromNBP();
        xml.setCurrency(currency);
        xml.setDateFrom(dateFrom);
        xml.setDateTo(dateTo);

        Rates output = xml.getCurrencyBetweenDates();

        System.out.println(output.getAverageBid());
        System.out.println(output.getStandardDeviation());

    }
}
