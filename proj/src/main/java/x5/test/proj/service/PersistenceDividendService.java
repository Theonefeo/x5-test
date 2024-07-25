package x5.test.proj.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import x5.test.proj.domain.Dividend;
import x5.test.proj.repository.DividendRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersistenceDividendService {
    final ParseDividendService parseDividendService;
    final DividendRepository dividendRepository;
    final KafkaTemplate<String, Dividend> kafkaTemplate;

    public List<Dividend> getDividends() {
        return dividendRepository.findAll();
    }

    @Transactional
    public void saveDividends() {
        dividendRepository.saveAll(parseDividendService.fetchAndParseData());
        log.info("Dividends info was updated in db");
    }

    public void sendDividendById(long id) {
        var dividend = dividendRepository.findById(id);

        if (dividend.isPresent()) {
            var dividendKey = UUID.randomUUID().toString();
            kafkaTemplate.send("dividend", dividendKey, dividend.get());
            log.info("Dividend with id \"{}\" was sent to kafka with key \"{}\"", id, dividendKey);
        } else {
            log.warn("No dividend with id: {} ", id);
        }
    }
}
