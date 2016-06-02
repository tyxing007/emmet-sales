package emmet.sales.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import emmet.common.message.ErrorMessage;
import emmet.core.data.entity.OrderProductItem;
import emmet.sales.order.repository.OrderProductItemRepsitory;
import emmet.sales.order.service.SalesOrderService;

@RepositoryRestController()
@RequestMapping("/salesSlip/")
public class SalesSlipController {


	@Autowired
	SalesOrderService salesOrderService;
	@Autowired
	OrderProductItemRepsitory productItemRepository;
	
	@RequestMapping(value = "/orderItemList/search/findBycustIdAndOrdId", method = RequestMethod.GET)
	public ResponseEntity<?> getCustList(@RequestParam String custId,
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
	



	

}
