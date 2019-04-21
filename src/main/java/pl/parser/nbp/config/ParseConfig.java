package pl.parser.nbp.config;

import org.jetbrains.annotations.Contract;

public class ParseConfig {
    protected String currency;
    protected String dateFrom;
    protected String dateTo;
    private static final String NBP_URL = "http://www.nbp.pl/kursy/xml/%s.xml";
    private static final String NBP_DIR_URL = "https://www.nbp.pl/kursy/xml/dir%s.txt";


    public ParseConfig(String currency, String dateFrom, String dateTo) {
        this.currency = currency;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public static String getNbpUrl() {
        return NBP_URL;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public static String getNbpDirUrl() {
        return NBP_DIR_URL;
    }
}
