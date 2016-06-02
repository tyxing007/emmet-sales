package emmet.sales.order.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import emmet.core.data.entity.Customer;
import emmet.core.data.entity.Employee;
import emmet.core.data.entity.FormNumber;
import emmet.core.data.entity.MaterialStock;
import emmet.core.data.entity.OrderProductItem;
import emmet.core.data.entity.SalesSlip;
import emmet.core.data.entity.SalesSlip.SalesSlipStatus;
import emmet.core.data.entity.SalesSlipDetail;
import emmet.core.data.entity.Warehouse;
import emmet.core.data.repository.EmployeeRepository;
import emmet.sales.order.exception.OperationNotPermitException;
import emmet.sales.order.model.CreateSalesSlipModel;
import emmet.sales.order.repository.OrderProductItemRepsitory;
import emmet.sales.order.repository.SalesSlipRepository;
import emmet.sales.order.repository.WarehouseRepository;


@Service
public class SalesSlipService {

	@Autowired
	private OrderProductItemRepsitory orderItemRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private SalesSlipRepository salesSlipRepository;
	@Autowired
	private WarehouseRepository warehouseRepository;
	
	
	@Transactional
	public SalesSlip  createNewSalesSlip(CreateSalesSlipModel model) throws OperationNotPermitException{
		
		//check employee
		Employee createUser = employeeRepository.findOne(model.getUserId());
		if(createUser==null){
			throw new OperationNotPermitException("can not find employee by id!");
		}
		
		//check warehouse
		Warehouse warehouse= warehouseRepository.findOne(model.getWarehouseId());
		if(warehouse==null){
			throw new  OperationNotPermitException("can not find warehouse by id!");
		}
		
		List<OrderProductItem> orderItemList = model.getOrderItemList();
		
		if(orderItemList==null||orderItemList.size()==0){
			throw new OperationNotPermitException("orderItemList has no data!");
		}
		
		//process orderItemList
		Customer customer =null;
		List<OrderProductItem> sourceOrderItemList =new ArrayList<OrderProductItem>();
		
		for(OrderProductItem orderItem:orderItemList){			
			
			OrderProductItem dbOrderItem=orderItemRepository.findOne(orderItem.getId());
			
			if(customer!=null){
				if(customer!=dbOrderItem.getOrder().getInfo().getCustomer()){
					throw new OperationNotPermitException("only the same customer,can can be create a sales slip!");
				}
			}
			customer=dbOrderItem.getOrder().getInfo().getCustomer();
			
			sourceOrderItemList.add(dbOrderItem);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		Calendar now = Calendar.getInstance();
		
		SalesSlip salesSlip =new SalesSlip();
		salesSlip.setCreateDate(now);
		salesSlip.setCreateUser(createUser);
		salesSlip.setFormDate(Date.valueOf(sdf.format(now.getTime())));		
		salesSlip.setNote("");
		salesSlip.setStatus(SalesSlipStatus.PROCESSING);
		
		salesSlipRepository.save(salesSlip);
		
		
		FormNumber formNumber = new FormNumber(); 
		formNumber.setId(salesSlip.getId());
		
		salesSlip.setFormNumber(formNumber);
		
		//detail
		List<SalesSlipDetail> salesSlipDetails= new ArrayList<SalesSlipDetail>();
		salesSlip.setSalesSlipDetails(salesSlipDetails);				
		
		for(OrderProductItem orderItem:sourceOrderItemList){
			SalesSlipDetail salesSlipDetail = new SalesSlipDetail();
			salesSlipDetail.setOrderItem(orderItem);
			salesSlipDetail.setSalesSlip(salesSlip);
			
			MaterialStock ms = new MaterialStock();			

			ms.setWarehouse(warehouse);
			ms.setMaterial(orderItem.getProduct().getMaterial());
			ms.setIoQty(BigDecimal.valueOf(orderItem.getQuantity()).abs().negate());
			ms.setFormNumber(formNumber);
			ms.setFormDate(salesSlip.getFormDate());
			ms.setEnabled(false);
			ms.setCreateDate(now);
			ms.setBatchNumber(null);			

			salesSlipDetail.setMaterialStock(ms);
			
			salesSlipDetails.add(salesSlipDetail);
			
		}
		
		
		return salesSlipRepository.save(salesSlip);
	}
	
}
