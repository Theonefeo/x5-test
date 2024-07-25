package x5.test.proj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import x5.test.proj.domain.Dividend;
import x5.test.proj.service.ParseDividendService;
import x5.test.proj.service.PersistenceDividendService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@AutoConfigureTestDatabase
class ProjApplicationTests {
	@Autowired
    ParseDividendService parserDividendService;
	@Autowired
    PersistenceDividendService persistenceDividendService;

    @Test
    void parserIsCorrect() {
        var dividends = parserDividendService.fetchAndParseData();

        assertThat(dividends).hasSizeGreaterThan(0);
        assertThat(dividends.get(0)).hasFieldOrPropertyWithValue("date", 1640984400L);
    }

    @Test
    void persistenceIsCorrect() {
        persistenceDividendService.saveDividends();

        Dividend divFirst = persistenceDividendService.getDividends().stream()
                .findFirst().orElseThrow(() -> new RuntimeException("Do not find data in H2 db"));

        assertEquals(1640984400L, divFirst.getDate());
        assertEquals(0, divFirst.getDateClosingRegistry());
        assertEquals(0, divFirst.getExDividendDate());
        assertEquals(0, divFirst.getT2Mode());
        assertEquals(0, divFirst.getPaymentDate());
        assertEquals(0, divFirst.getAmountDividends());
        assertEquals(0, divFirst.getPerGPR());
    }
}
