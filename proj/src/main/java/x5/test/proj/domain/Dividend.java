package x5.test.proj.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "PUBLIC", name = "dividends")
public class Dividend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long date;
    private Long dateClosingRegistry;
    private Long exDividendDate;
    private Long t2Mode; //Последний день для покупки ГДР с дивидендами в режиме Т+2
    private Long paymentDate;
    private Double amountDividends;
    private Double perGPR; //Дивиденды на одну ГДР, руб.
}