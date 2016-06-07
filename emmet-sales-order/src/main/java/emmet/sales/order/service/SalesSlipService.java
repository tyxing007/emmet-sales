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
import emmet.partner.entity.PartnerCorporation;
import emmet.sales.order.exception.OperationNotPermitException;
import emmet.sales.order.model.CreateSalesSlipModel;
import emmet.sales.order.model.CustomerModel;
import emmet.sales.order.model.MaterialWarehouseStockModel;
import emmet.sales.order.model.NormalOrderItemModel;
import emmet.sales.order.repository.BatchNumberRepository;
import emmet.sales.order.repository.CorporationRepository;
import emmet.sales.order.repository.CustomerRepository;
import emmet.sales.order.repository.EmployeeRepository;
import emmet.sales.order.repository.MaterialStockRepository;
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
	@Autowired
	OrderProductItemRepsitory productItemRepository;
	@Autowired
	CorporationRepository corporationRepository;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	MaterialStockRepository materialStockRepository;
	
	@Transactional
	public SalesSlip  createNewSalesSlip(CreateSalesSlipModel model) throws OperationNotPermitException{
		
		//check employee
		Employee createUser = employeeRepository.findOne(model.getUserId());
		if(createUser==null){
			throw new OperationNotPermitException("can not find employee by id!");
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
			
			//check ordItem Stock
			BigDecimal ordItemStock = null;
			if(Boolean.TRUE.equals(orderItem.getProduct().getMaterial().getBatchNoCtr())){
				ordItemStock = materialStockRepository.getAvailableStockWithBatchNo(orderItem.getProduct().getMaterial(), this.getBatchNumber(orderItem));
			}else{
				ordItemStock = materialStockRepository.getAvailableStockNoBatchNo(orderItem.getProduct().getMaterial());
			}
			if(ordItemStock.longValue()<=0){
				throw new OperationNotPermitException(orderItem.getProduct().getId()+"'s stock less than 1 !");
			}
			
			//total應銷數
			Integer totalSalesQty = orderItem.getQuantity()-orderItem.getSoldQty();
			//total可銷數(庫存數)
			Integer totalStock = ordItemStock.intValue();
			//this total qty 本次銷貨數
			Integer thisTotalQty;
			
			if(totalSalesQty>=totalStock){
				thisTotalQty=totalStock;
			}else{
				thisTotalQty=totalSalesQty;
			}
			//商品可用庫存清單
			List<MaterialWarehouseStockModel> MaterialWarehouseStockList = this.getMaterialWarehouseStockList(orderItem.getProduct().getMaterial(), this.getBatchNumber(orderItem));

			
			int start = 0;
			while(thisTotalQty>0){
				MaterialWarehouseStockModel mws = MaterialWarehouseStockList.get(start);
				
				//依據倉庫數量由庫存量少到多開始扣帳
				SalesSlipDetail salesSlipDetail = new SalesSlipDetail();
				salesSlipDetail.setOrderItem(orderItem);
				salesSlipDetail.setSalesSlip(salesSlip);
				
				MaterialStock ms = new MaterialStock();			

				ms.setWarehouse(mws.getWarehouse());
				ms.setMaterial(orderItem.getProduct().getMaterial());
				
				if(mws.getStock().longValue() > thisTotalQty){
					ms.setIoQty(BigDecimal.valueOf(thisTotalQty.doubleValue()).negate());
				}else{
					ms.setIoQty(mws.getStock().abs().negate());
				}
				
				ms.setFormNumber(formNumber);
				ms.setFormDate(salesSlip.getFormDate());
				ms.setEnabled(false);
				ms.setCreateDate(now);
				if(Boolean.TRUE.equals(orderItem.getProduct().getMaterial().getBatchNoCtr())){
					ms.setBatchNumber(this.getBatchNumber(orderItem));		
				}
					
				salesSlipDetail.setMaterialStock(ms);
				
				salesSlipDetails.add(salesSlipDetail);
				
				
				thisTotalQty = thisTotalQty - mws.getStock().abs().intValue();
				start=start+1;
			}
				
			
			
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
	
	public List<CustomerModel> getCustomerList(){
		
		List<String> custIdList = productItemRepository.findSalesSlipCustList();
		List<CustomerModel> custList = new ArrayList<CustomerModel>();
		for(String custId:custIdList){
			Customer customer = customerRepository.findOne(custId);					
			PartnerCorporation corporation =  corporationRepository.findByPartnerId(customer.getPartner().getId());
			
			CustomerModel model = new CustomerModel();
			model.setCorporation(corporation);
			model.setCustomer(customer);
			custList.add(model);
						
		}
		
		return custList;
	}
	
	public List<NormalOrderItemModel> getCustOrderItemList(String custId,String ordId) throws OperationNotPermitException{
		
		if(custId==null){
			custId="";
		}
		
		if(ordId==null){
			ordId="";
		}
				
		List<OrderProductItem> productItemList= productItemRepository.findNormalOrderItemByCustAndOrdId(custId, "%"+ordId.trim()+"%");
		List<NormalOrderItemModel> modelList = new ArrayList<NormalOrderItemModel>();
	
		for(OrderProductItem ordItem:productItemList){
			NormalOrderItemModel model = new NormalOrderItemModel();
			model.setOrderProductItem(ordItem);
			BigDecimal ordItemStock = null;
			if(Boolean.TRUE.equals(ordItem.getProduct().getMaterial().getBatchNoCtr())){
				ordItemStock = materialStockRepository.getAvailableStockWithBatchNo(ordItem.getProduct().getMaterial(), this.getBatchNumber(ordItem));
			}else{
				ordItemStock = materialStockRepository.getAvailableStockNoBatchNo(ordItem.getProduct().getMaterial());
			}

			model.setAvailableStock(ordItemStock);
			
			modelList.add(model);
		}
		
		return modelList;
	}
	
	private List<MaterialWarehouseStockModel> getMaterialWarehouseStockList(Material material,BatchNumber batchNumber) throws OperationNotPermitException{
		if(material==null||batchNumber==null){
			throw new OperationNotPermitException("material or batchNumber is null!");
		}
		List<MaterialWarehouseStockModel> warehouseStockList = materialStockRepository.getMaterialStockList(material, batchNumber);
		return warehouseStockList;
	}
	
	
}
