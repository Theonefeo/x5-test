package x5.test.proj.rest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import x5.test.proj.service.ParseDividendService;
import x5.test.proj.service.PersistenceDividendService;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DividendController {
    final ParseDividendService parseDividendService;
    final PersistenceDividendService persistenceDividendService;

    //Запрос на парсинг
    @GetMapping("/dividends")
    private ResponseEntity<?> getDividendData() {
        return ResponseEntity.ok(parseDividendService.fetchAndParseData());
    }

    //Запрос на сохранение в H2
    @PutMapping("/dividends")
    @ResponseStatus(HttpStatus.ACCEPTED)
    private void putDividendData() {
        persistenceDividendService.saveDividends();
    }

    //Запрос на отправку в Kafka
    @GetMapping("/dividend")
    @ResponseStatus(HttpStatus.OK)
    private void sendToKafkaDividendById(@RequestParam long id) {
        persistenceDividendService.sendDividendById(id);
    }
}
