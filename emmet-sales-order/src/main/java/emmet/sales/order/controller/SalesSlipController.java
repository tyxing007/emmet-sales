package emmet.sales.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import emmet.common.message.ErrorMessage;
import emmet.core.data.entity.OrderProductItem;
import emmet.sales.order.exception.OperationNotPermitException;
import emmet.sales.order.model.CreateSalesSlipModel;
import emmet.sales.order.repository.OrderProductItemRepsitory;
import emmet.sales.order.service.SalesSlipService;

@RepositoryRestController()
@RequestMapping("/salesSlip/")
public class SalesSlipController {


	@Autowired
	SalesSlipService salesSlipService;
	@Autowired
	OrderProductItemRepsitory productItemRepository;
	
	@RequestMapping(value = "/orderItemList/search/findByCustIdAndOrdId", method = RequestMethod.GET)
	public ResponseEntity<?> getOrderItemList(@RequestParam String custId,
			@RequestParam(required=false) String ordId){
		
		if(ordId==null){
			ordId="";
		}
		
		List<OrderProductItem> productItemList =null;
		try {
			productItemList= productItemRepository.findNormalOrderItemByCustAndOrdId(custId, "%"+ordId.trim()+"%");
		} catch (Exception e) {
			ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,e,"/orderItemList/search/findBycustIdAndOrdId");
			return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.BAD_REQUEST);
		}
		
					
		return ResponseEntity.ok(productItemList);
		
	}
	
	@RequestMapping(value = "/createSalesSlip", method = RequestMethod.POST)
	public ResponseEntity<?> createSalesSlip(@RequestBody CreateSalesSlipModel model){
		try {
			return ResponseEntity.ok(salesSlipService.createNewSalesSlip(model));
		} catch (OperationNotPermitException e) {
			ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,e,"/createSalesSlip");
			return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.BAD_REQUEST);
		}
	}
	


	@RequestMapping(value = "/customerList/", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerList(){
		try {
			return ResponseEntity.ok(salesSlipService.getCustomerList());
		} catch (Exception e) {
			ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,e,"/customerList/");
			return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.BAD_REQUEST);
		}
	}
	
	

	

}
