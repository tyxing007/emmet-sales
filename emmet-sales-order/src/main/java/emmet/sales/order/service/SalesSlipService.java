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

import emmet.core.data.entity.BatchNumber;
import emmet.core.data.entity.Customer;
import emmet.core.data.entity.Employee;
import emmet.core.data.entity.FormNumber;
import emmet.core.data.entity.Material;
import emmet.core.data.entity.MaterialStock;
import emmet.core.data.entity.Order;
import emmet.core.data.entity.Order.OrderStatus;
import emmet.core.data.entity.OrderProductItem;
import emmet.core.data.entity.SalesSlip;
import emmet.core.data.entity.SalesSlip.SalesSlipStatus;
import emmet.core.data.entity.SalesSlipDetail;
import emmet.core.data.entity.Warehouse;
import emmet.sales.order.exception.OperationNotPermitException;
import emmet.sales.order.model.CreateSalesSlipModel;
import emmet.sales.order.repository.BatchNumberRepository;
import emmet.sales.order.repository.EmployeeRepository;
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
	@Autowired
	private BatchNumberRepository batchNumberRepository;
	
	
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
			throw new  OperationNotPermitException("can not find warehouse by id: "+ model.getWarehouseId()+" !");
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
			if(!OrderStatus.CONFIRMED.equals(dbOrderItem.getOrder().getStatus())){
				throw new OperationNotPermitException("only oder status is confirmed,can be create a sales slip");
			}
			
			customer=dbOrderItem.getOrder().getInfo().getCustomer();
			sourceOrderItemList.add(dbOrderItem);
		}
		
		if(sourceOrderItemList.size()==0){
			throw new OperationNotPermitException("please choose some order Item for create sales slip !");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		Calendar now = Calendar.getInstance();
		
		SalesSlip salesSlip =new SalesSlip();
		salesSlip.setCreateDate(now);
		salesSlip.setCreateUser(createUser);
		salesSlip.setFormDate(Date.valueOf(sdf.format(now.getTime())));		
		salesSlip.setNote("");
		salesSlip.setStatus(SalesSlipStatus.PROCESSING);
		salesSlip.setCustomer(sourceOrderItemList.get(0).getOrder().getInfo().getCustomer());
		
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
			
			ms.setBatchNumber(this.getBatchNumber(orderItem));			

			salesSlipDetail.setMaterialStock(ms);
			
			salesSlipDetails.add(salesSlipDetail);
			
		}
		
		
		return salesSlipRepository.save(salesSlip);
	}
	
	
	private BatchNumber getBatchNumber(OrderProductItem ordItem) throws OperationNotPermitException{
		
		BatchNumber batchNumber=null;
		
		Order order = ordItem.getOrder();
		if(order==null){
			throw new OperationNotPermitException("can not find order by id");
		}
		
		Material material = ordItem.getProduct().getMaterial();
		
		
		if(Boolean.TRUE.equals(material.getBatchNoCtr())){
			//find a BatchNumber
			batchNumber = batchNumberRepository.findByMaterialAndCode(material, order.getId());
					
			//if no BatchNumber , create one
			if(batchNumber == null){
				batchNumber= new BatchNumber();
				batchNumber.setCode(order.getId());
				batchNumber.setMaterial(material);
				
				batchNumberRepository.save(batchNumber);
			}
		}
				
		return batchNumber;
	}
	
}
