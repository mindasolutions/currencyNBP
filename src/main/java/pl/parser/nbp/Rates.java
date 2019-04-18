package pl.parser.nbp;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

@XmlRootElement
public class Rates {
    private List<Rate> rate = new LinkedList<>();

    @XmlElement
    public List<Rate> getRate() {
        return rate;
    }

    @XmlElement
    public void setRate(Rate rate) {
        this.rate.add(rate);
    }

    private double getAverage(String type) {
        double sum = 0;
        for (Rate r : rate) {
            switch (type) {
                case "bid":
                    sum += r.getBid();
                    break;
                case "ask":
                    sum += r.getAsk();
                    break;
            }
        }
        return new BigDecimal((sum / rate.size()), new MathContext(5)).doubleValue();
    }

    public double getAverageBid() {
        return getAverage("bid");
    }

    public double getAverageAsk() {
        return getAverage("ask");
    }


    public double getStandardDeviation() {
        double avg = getAverageAsk();
        double sum = 0;
        for (Rate r : rate) {
            sum += pow(r.getAsk() - avg, 2);
        }

        sum = sum / rate.size();

        return new BigDecimal(sqrt(sum), new MathContext(3)).doubleValue();
    }
}
