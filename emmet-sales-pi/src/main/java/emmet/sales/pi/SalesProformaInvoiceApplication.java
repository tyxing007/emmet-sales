package emmet.sales.pi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages={"emmet"})
public class SalesProformaInvoiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesProformaInvoiceApplication.class, args);
    }
}
