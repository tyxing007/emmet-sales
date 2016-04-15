package emmet.sales.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import emmet.core.data.entity.Order;
import emmet.sales.order.model.SalesOrderCreateModel;
import emmet.sales.order.repository.SalesOrderRepsitory;
import emmet.sales.order.service.SalesOrderService;

@RepositoryRestController()
@RequestMapping("/orders/")
public class SalesOrderController {


	@Autowired
	SalesOrderService salesOrderService;
	@Autowired
	SalesOrderRepsitory salesOrderRepsitory;
	
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
	
	@RequestMapping(value = "/search/findByIdLike", method = RequestMethod.GET)
	public ResponseEntity<?> createOrderFromPI(@RequestParam("id") String id ,Pageable page){
				
		try {
			return ResponseEntity.ok(salesOrderRepsitory.findByIdLike(id, page));
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
				
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateOrder(@RequestBody  Order order,@PathVariable String id){
		
		
		try {
			return ResponseEntity.ok(salesOrderService.updateOrder(order, id));
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
			
		
		
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllOrders(Pageable page){
				
		try {
			return ResponseEntity.ok(salesOrderRepsitory.findAll(page));
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
							
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> listAllOrders(@PathVariable String id){
				
		try {
			return ResponseEntity.ok(salesOrderRepsitory.findOne(id));
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
							
		
	}

}
