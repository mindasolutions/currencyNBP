import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.parser.nbp.ParseXMLFromNBP;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MyTests {
    static ParseXMLFromNBP tester = new ParseXMLFromNBP();

    @BeforeAll
    public static void setUp() throws Exception {
        // MyClass is tested

        tester.setCurrency("eur");
        tester.setDateFrom("2013-01-28");
        tester.setDateTo("2013-01-31");
    }

    @Test
    public void avgForCurrencyBidShouldHaveCorrectValue() throws Exception {

        // assert statements
        assertEquals(4.1505, tester.getCurrencyBetweenDates().getAverageBid(), "AVG Bid for EUR from 2013-01-28 to 2013-01-31 should be 4.1505");
    }

    @Test
    public void standardDeviationAskForCurrencyShouldHaveCorrectValue() throws Exception {

        // assert statements
        assertEquals(0.0125, tester.getCurrencyBetweenDates().getStandardDeviation(), "StandardDeviation for EUR from 2013-01-28 to 2013-01-31 should be 0.0125");
    }
}
