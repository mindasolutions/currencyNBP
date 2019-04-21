package pl.parser.nbp;

import pl.parser.nbp.validate.Validate;

public class MainClass {

    public static void main(String[] args) throws Exception {
        Validate.validateArgs(args);
        final String currency = args[0];
        final String dateFrom = args[1];
        final String dateTo = args[2];

        NbpFormatParser xml = new NbpFormatParser(currency, dateFrom, dateTo);

        Rates output = xml.getCurrencyBetweenDates();

        System.out.println(output.getAverageBid());
        System.out.println(output.getStandardDeviation());

    }
}
