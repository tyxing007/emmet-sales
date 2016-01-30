package emmet.sales.shipment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EntityScan({"emmet.sales.entity.order"})
public class SalesShipmantApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesShipmantApplication.class, args);
    }
}
