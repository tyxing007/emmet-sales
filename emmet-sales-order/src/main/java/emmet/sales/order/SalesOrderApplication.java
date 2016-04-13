package emmet.sales.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;

@SpringBootApplication
@EntityScan(basePackages={"emmet"})
public class SalesOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesOrderApplication.class, args);
    }
}
