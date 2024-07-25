package x5.test.proj;

/* Author: Develop by Fedor Karpov - @theonefeo
* Tests proj for X5.
*
* All rights reserved
* */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import x5.test.proj.service.PersistenceDividendService;

@SpringBootApplication
public class ProjApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(ProjApplication.class, args);
        var persistenceDividendService = (PersistenceDividendService) context.getBean("persistenceDividendService");
        persistenceDividendService.saveDividends();
    }
}
