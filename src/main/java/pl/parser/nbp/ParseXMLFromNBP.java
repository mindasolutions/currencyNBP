package pl.parser.nbp;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ParseXMLFromNBP {
    final static String DATE_FORMAT = "yyyy-MM-dd";
    private String currency;
    private String dateFrom;
    private String dateTo;
    private NodeList readFromUrl;
    private List<String> files = new ArrayList<>();

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

    public void setDateTo(String dateTo) throws ParseException {
        this.dateTo = dateTo;
    }

    public List<String> getFiles() {
        return files;
    }

    public Rates getCurrencyBetweenDates() throws Exception, ParserConfigurationException, SAXException {
        fileList(getDateFrom(), getDateTo(), null);

        Rates rates = new Rates();

        for (String list : getFiles()) {
            String url = "http://www.nbp.pl/kursy/xml/" + list + ".xml";
            readFromUrl = buildXML(url);

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

    private NodeList buildXML(String url) throws Exception, ParserConfigurationException, SAXException {
        NodeList rates = null;
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

    private List fileList(String startDate, String endDate, String currentYear) throws ParseException {

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

            URL url = new URL("https://www.nbp.pl/kursy/xml/dir" + currentYear + ".txt");

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (!Character.toString(line.charAt(0)).equals("c"))
                    continue;
                Integer date = Integer.parseInt(line.substring(5, 11));
                if (date >= Integer.parseInt(startDate) && date <= Integer.parseInt(endDate))
                    files.add(line);
                if (date >= Integer.parseInt(endDate))
                    break;
            }
            in.close();

            if (!currentYear.equals("") && (Integer.parseInt(endDate.substring(0, 2)) + 2000) >= (Integer.parseInt(currentYear) + 1)) {
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
}
