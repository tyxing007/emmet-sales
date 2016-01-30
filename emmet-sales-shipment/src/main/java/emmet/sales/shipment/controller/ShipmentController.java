package emmet.sales.shipment.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import emmet.sales.entity.order.ShipmentType;

@RestController
@RequestMapping("/order/shipment")
public class ShipmentController {

	
	@RequestMapping(value="/types", method=RequestMethod.GET)
	public List<ShipmentType> getAllShipmentType(){
		
		List<ShipmentType> result  = new ArrayList<ShipmentType>();
		
		ShipmentType st = new ShipmentType();
		st.setId(1);
		st.setName("宅配");
		result.add(st);
	
		st.setId(2);
		st.setName("自取");
		result.add(st);
		
		st.setId(3);
		st.setName("送貨到府");
		result.add(st);
		
		st.setId(4);
		st.setName("貨運");
		result.add(st);
		
		st.setId(5);
		st.setName("郵寄");
		result.add(st);
		
		return result;
	}

}
