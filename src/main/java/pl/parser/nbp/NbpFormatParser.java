package pl.parser.nbp;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import pl.parser.nbp.config.ParseConfig;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NbpFormatParser extends ParseConfig {
    private NodeList readFromUrl;
    private List<String> files = new ArrayList<>();

    public NbpFormatParser(String currency, String dateFrom, String dateTo) {
        super(currency, dateFrom, dateTo);
    }

    public List<String> getFiles() {
        return files;
    }

    public Rates getCurrencyBetweenDates() throws Exception {
        fileList(getDateFrom(), getDateTo(), null);

        Rates rates = new Rates();

        for (String list : getFiles()) {
            readFromUrl = buildXML(String.format(getNbpUrl(), list));

            for (int i = 0; i < this.readFromUrl.getLength(); i++) {
                NodeList rate = this.readFromUrl.item(i).getChildNodes();

                Rate rateObj = new Rate();

                if (rate.item(5).getTextContent().equals(getCurrency().toUpperCase())) {

                    rateObj.setBid(Double.parseDouble(rate.item(7).getTextContent().replaceAll(",", ".")));
                    rateObj.setAsk(Double.parseDouble(rate.item(9).getTextContent().replaceAll(",", ".")));
                    rates.setRate(rateObj);
                }
            }
        }
        return rates;
    }

    private NodeList buildXML(String url) throws Exception {
        NodeList rates;
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setNamespaceAware(false);
        f.setValidating(false);

        DocumentBuilder b = f.newDocumentBuilder();

        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
        urlConnection.addRequestProperty("Content-Type", "application/xml");

        try {
            Document doc = b.parse(urlConnection.getInputStream());
            doc.getDocumentElement().normalize();
            rates = doc.getElementsByTagName("pozycja");
        } catch (Exception e) {
            throw new Exception("Response code: " + urlConnection.getResponseCode() + "\n" + e.getMessage());
        }

        return rates;
    }

    private List fileList(String startDate, String endDate, String currentYear) {
        try {
            String thisYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
            if (currentYear == null) {
                startDate = DateParse.parseDate(startDate);
                endDate = DateParse.parseDate(endDate);
                currentYear = Integer.toString(Integer.parseInt(startDate.substring(0, 2)) + 2000);
            }
            if (thisYear.equals(currentYear)) {
                currentYear = "";
            }

            Integer fromDate = Integer.parseInt(startDate);
            Integer toDate = Integer.parseInt(endDate);

            URL url = new URL(String.format(getNbpDirUrl(), currentYear));

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (!Character.toString(line.charAt(0)).equals("c"))
                    continue;
                Integer date = Integer.parseInt(line.substring(5, 11));
                if (isDateBetween(date, fromDate, toDate))
                    files.add(line);
                if (isDateBefore(date, toDate))
                    break;
            }
            in.close();

            if (isCuurentYearAfter(currentYear, endDate)) {
                currentYear = Integer.toString((Integer.parseInt(currentYear) + 1));
                fileList(startDate, endDate, currentYear);
            }

        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }

        return files;
    }

    private boolean isDateBetween(int date, int fromDate, int toDate) {
        return (date >= fromDate && date <= toDate);
    }

    private boolean isDateBefore(int date, int toDate) {
        return (date >= toDate);
    }

    private boolean isCuurentYearAfter(String currentYear, String endDate) {
        return (!currentYear.equals("") && (Integer.parseInt(endDate.substring(0, 2)) + 2000) >= (Integer.parseInt(currentYear) + 1));
    }
}
