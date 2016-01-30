package emmet.sales.shipment.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import emmet.sales.shipment.entity.ShippingCompany;

@RestController
@RequestMapping("/order/shipment")
public class ShippingCompanyController {

	@RequestMapping(value="/shippingCompany", method=RequestMethod.GET)
	public List<ShippingCompany> getAll() {
		
		ShippingCompany sp1 = new ShippingCompany();
		sp1.setId(1);
		sp1.setName("DHL");

		ShippingCompany sp2 = new ShippingCompany();
		sp2.setId(2);
		sp2.setName("UPS");

		ShippingCompany sp3 = new ShippingCompany();
		sp3.setId(3);
		sp3.setName("TNT");

		return Arrays.asList(new ShippingCompany[] { sp1, sp2, sp3 });

	}

}
