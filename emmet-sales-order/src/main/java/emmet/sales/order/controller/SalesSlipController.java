package emmet.sales.order.controller;

import java.util.List;

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

import emmet.common.message.ErrorMessage;
import emmet.core.data.entity.SalesSlip;
import emmet.sales.order.exception.OperationNotPermitException;
import emmet.sales.order.model.CreateSalesSlipModel;
import emmet.sales.order.model.NormalOrderItemModel;
import emmet.sales.order.model.SalesSlipModel;
import emmet.sales.order.model.SetStatusModel;
import emmet.sales.order.repository.OrderProductItemRepsitory;
import emmet.sales.order.repository.SalesSlipRepository;
import emmet.sales.order.service.SalesSlipService;

@RepositoryRestController()
@RequestMapping("/salesSlips/")
public class SalesSlipController {


	@Autowired
	SalesSlipService salesSlipService;
	@Autowired
	OrderProductItemRepsitory productItemRepository;
	@Autowired
	SalesSlipRepository salesSlipRepository;
	
	@RequestMapping(value = "/orderItemList/search/findByCustIdAndOrdId", method = RequestMethod.GET)
	public ResponseEntity<?> getOrderItemList(@RequestParam String custId,
			@RequestParam(required=false) String ordId){
		
		List<NormalOrderItemModel> modelList =null;
		try {

			modelList=salesSlipService.getCustOrderItemList(custId, ordId);
		} catch (Exception e) {
			ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,e,"/orderItemList/search/findBycustIdAndOrdId");
			return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.BAD_REQUEST);
		}
							
		return ResponseEntity.ok(modelList);
		
	}
	
	@RequestMapping(value = "/createSalesSlip", method = RequestMethod.POST)
	public ResponseEntity<?> createSalesSlip(@RequestBody CreateSalesSlipModel model){
		try {
			
			SalesSlip salesSlip =salesSlipService.createNewSalesSlip(model);
			return ResponseEntity.ok(salesSlipService.getSalesSlipModel(salesSlip));
		} catch (OperationNotPermitException e) {
			ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,e,"/createSalesSlip");
			return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.BAD_REQUEST);
		}
	}
	


	@RequestMapping(value = "/customerList/search/findByIdLike", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerList(@RequestParam(required=false) String id){
		try {
			return ResponseEntity.ok(salesSlipService.getCustomerList(id));
		} catch (Exception e) {
			ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,e,"/customerList/search/findByIdLike");
			return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.BAD_REQUEST);
		}
	}
	
	

	@RequestMapping(value = "/search/findByIdLike", method = RequestMethod.GET)
	public ResponseEntity<?> getSalesSlipList(@RequestParam(required=false) String id,Pageable page){
		try {
			if(id==null){
				id="";
			}
			return ResponseEntity.ok(salesSlipRepository.findByIdLike("%"+id.trim()+"%", page));
		} catch (Exception e) {
			ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,e,"/search/findByIdLike");
			return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getSalesSlipById(@PathVariable String id){
		try {
			SalesSlip salesSlip = salesSlipRepository.findOne(id);
			SalesSlipModel model = salesSlipService.getSalesSlipModel(salesSlip);
			model.getSalesSlip().setSalesSlipDetails(null);
			return ResponseEntity.ok(model);
		} catch (Exception e) {
			ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,e,"/{id}");
			return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> UpdateSalesSlip(@PathVariable String id,SalesSlip salesSlip){
		try {
			SalesSlip reaultSalesSlip = salesSlipService.updateSalesSlip(salesSlip);
			SalesSlipModel model = salesSlipService.getSalesSlipModel(reaultSalesSlip);
			return ResponseEntity.ok(model);
		} catch (Exception e) {
			ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,e,"/{id}");
			return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/{id}/setStatus", method = RequestMethod.PUT)
	public ResponseEntity<?> setSalesSlipStatus(@PathVariable String id,SetStatusModel model){
		try {
			SalesSlip reaultSalesSlip = salesSlipService.setSalesSlipStatus(id, model);
			SalesSlipModel ssmodel = salesSlipService.getSalesSlipModel(reaultSalesSlip);
			return ResponseEntity.ok(ssmodel);
		} catch (Exception e) {
			ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,e,"/{id}");
			return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.BAD_REQUEST);
		}
	}
	

}
