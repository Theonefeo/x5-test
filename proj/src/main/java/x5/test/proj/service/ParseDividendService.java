package x5.test.proj.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import x5.test.proj.domain.Dividend;
import x5.test.proj.repository.DividendRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParseDividendService {
    static final String DIVIDEND_INFO_URL = "https://www.x5.ru/ru/investors/dividends/";

    final RestTemplate restTemplate;

    public List<Dividend> fetchAndParseData() {
        List<Dividend> dividends = new ArrayList<>();

        var response = restTemplate.getForEntity(DIVIDEND_INFO_URL, String.class);
        String responseBody = response.getBody();

        if (responseBody == null) {
            log.error("Response body is null. No data could be parsed");
            return dividends;
        }
        Document document = Jsoup.parse(responseBody);
        Elements tableDividends = document
                .select("figure.wp-block-table.is-style-stripes.is-long-format-on-mobile.is-header-grey")
                .select("table")
                .select("tbody");

        for (Element rowDiv : tableDividends.first().children()) {
//            Element cells = List<String> dateOffYearDivList = (List<String>) rowDiv.select("td").stream().map(x -> x.text());

            Dividend dividendOnYear = new Dividend();

            Elements arraysRowDivCells = rowDiv.children();
            for (int i = 0; i < rowDiv.childNodeSize(); i++) {

                String textCell = arraysRowDivCells.get(i).text();
                long timeStamp = 0;
                double div = 0;
                long sumDiv = 0;


                /**
                 * Парсит и чистит данные от спецсимволов.
                 * возвращает цифровое значение цены либо времени (в TimeStamp) через переменные div или timeStamp.
                 */
                String cellCorrectText = textCell.replaceAll("\\*\\*", "")
                        .replaceAll("&nbsp", "")
                        .replaceAll("&", "")
                        .replaceAll(";", "")
                        .replaceAll("до", "")
                        .replaceAll("мес", "").replaceAll("\s*", "");


                if (cellCorrectText.contains(".")) {
                    if (cellCorrectText.matches("\\d{1,2}.\\d\\d{1,2}.\\d{4}"))
                        timeStamp = convertDateStringToUnixTimestamp(cellCorrectText, "dd.MM.yyyy");
                    else if (cellCorrectText.matches("\\d{1,2}.\\d{4}"))
                        timeStamp = convertDateStringToUnixTimestamp(cellCorrectText, "MM.yyyy");
                    else if (cellCorrectText.contains("—"))
                        log.info("Dividends were not paid");
                } else if (!cellCorrectText.isBlank() & !cellCorrectText.contains("—") & !(i == 5 | i == 6)) {
                    timeStamp = convertDateStringToUnixTimestamp("1." + cellCorrectText, "MM.yyyy");
                } else if (cellCorrectText.contains("—")) {
                    log.info("Dividends were not paid");
                } else {
                    String strValue = cellCorrectText.replaceAll("\s*", "").replaceAll(",", ".");
                    div = Double.parseDouble(strValue);
                }

//                Long result = (textCell -> textCell.contains("—")) ? 0 : Long.parseLong(textCell);

                switch (i) {
                    case 0:
                        System.out.println("date");
                        dividendOnYear.setDate(timeStamp);
                        break;
                    case 1:
                        System.out.println("dateClosingRegistry");
                        dividendOnYear.setDateClosingRegistry(timeStamp);
                        break;
                    case 2:
                        System.out.println("exDividendDate");
                        dividendOnYear.setExDividendDate(timeStamp);
                        break;
                    case 3:
                        System.out.println("t2Mode");
                        dividendOnYear.setT2Mode(timeStamp);
                        break;
                    case 4:
                        System.out.println("paymentDate");
                        dividendOnYear.setPaymentDate(timeStamp);
                        break;
                    case 5:
                        System.out.println("amountDividends");
                        dividendOnYear.setAmountDividends(div);
                        break;
                    case 6:
                        System.out.println("perGDR");
                        dividendOnYear.setPerGPR(div);
                        break;
                    default:
                        log.info("No elements found for parsing");
                        break;
                }
            }

            dividends.add(dividendOnYear);
            System.out.println("Div on YEAR: " + dividendOnYear);
        }

        return dividends;
    }

    /**
     * Преобразует строку даты в формат Unix timestamp.
     *
     * @param dateString Строка даты в формате "pattern".
     * @param pattern    Шаблон даты, например, "MM.yyyy" или "dd.MM.yyyy".
     * @return Unix timestamp в секундах.
     */
    public long convertDateStringToUnixTimestamp(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            Date date = dateFormat.parse(dateString);
            return date.getTime() / 1000;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date string: " + dateString, e);
        }
    }
}

