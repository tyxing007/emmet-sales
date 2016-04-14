package emmet.sales.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import emmet.core.data.entity.Order;
import emmet.sales.order.model.SalesOrderCreateModel;
import emmet.sales.order.service.SalesOrderService;

@RepositoryRestController()
@RequestMapping("/orders")
public class SalesOrderController {


	@Autowired
	SalesOrderService salesOrderService;
	
	@RequestMapping(value = "/createFromPI", method = RequestMethod.POST)
	public ResponseEntity<?> createOrderFromPI(@RequestBody SalesOrderCreateModel model){
		
		Order order = null ;
		
		try {
			order = salesOrderService.createOrderByPIVersion(model);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
			
		return ResponseEntity.ok(order);
		
	}



}
