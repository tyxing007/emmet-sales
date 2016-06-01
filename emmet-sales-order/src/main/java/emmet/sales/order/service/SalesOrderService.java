package emmet.sales.order.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import emmet.core.data.entity.Order;
import emmet.core.data.entity.Order.OrderStatus;
import emmet.core.data.entity.OrderExtraCharge;
import emmet.core.data.entity.OrderInfo;
import emmet.core.data.entity.OrderProductItem;
import emmet.core.data.entity.OrderProductItem.OrderItemStatus;
import emmet.core.data.entity.OrderShipping;
import emmet.sales.entity.pi.ProformaInvoice.ProformainvoiceStatus;
import emmet.sales.entity.pi.ProformaInvoiceExtraCharge;
import emmet.sales.entity.pi.ProformaInvoiceInfo;
import emmet.sales.entity.pi.ProformaInvoiceProductItem;
import emmet.sales.entity.pi.ProformaInvoiceShipping;
import emmet.sales.entity.pi.ProformaInvoiceVersion;
import emmet.sales.order.exception.DataNotFoundException;
import emmet.sales.order.exception.OperationNotPermitException;
import emmet.sales.order.model.SalesOrderCreateModel;
import emmet.sales.order.model.SetStatusModel;
import emmet.sales.order.repository.OrderExtraChargeRepsitory;
import emmet.sales.order.repository.OrderInfoRepsitory;
import emmet.sales.order.repository.OrderProductItemRepsitory;
import emmet.sales.order.repository.OrderShippingRepsitory;
import emmet.sales.order.repository.ProformaInvoiceVersionRepsitory;
import emmet.sales.order.repository.SalesOrderRepsitory;

@Service
public class SalesOrderService {

	@Autowired
	SalesOrderRepsitory salesOrderRepsitory;	
	@Autowired
	ProformaInvoiceVersionRepsitory proformaInvoiceVersionRepsitory;	
	@Autowired
	OrderInfoRepsitory orderInfoRepsitory;
	@Autowired
	OrderExtraChargeRepsitory orderExtraChargeRepsitory;
	@Autowired
	OrderShippingRepsitory orderShippingRepsitory;
	@Autowired
	OrderProductItemRepsitory orderProductItemRepsitory;
	
	@Transactional
	public Order createOrderByPIVersion(SalesOrderCreateModel model) throws Exception {
		
		ProformaInvoiceVersion	proformaInvoiceVersion = proformaInvoiceVersionRepsitory.findOne(model.getPiVersionId());
		if(proformaInvoiceVersion == null){
			throw new DataNotFoundException("can not find pi verson");
		}
		
		List<ProformaInvoiceVersion>  dbPIList= proformaInvoiceVersionRepsitory.findByProformaInvoiceIdAndOrderIsNotNull(proformaInvoiceVersion.getProformaInvoice().getId());

		if(dbPIList.size()>0){
			throw new OperationNotPermitException("the pi has order,can not generate a new order");
		}
		
		Calendar now = Calendar.getInstance();
		
		Order order = new Order();
		proformaInvoiceVersion.setOrder(order);
		proformaInvoiceVersion.setStatus(ProformainvoiceStatus.ORDERED);
		order.setCanceled(Boolean.FALSE);
		order.setOrderDate(now);
		order.setStatus(OrderStatus.PROCESSING);
		
		OrderInfo orderInfo = new OrderInfo();
		order.setInfo(orderInfo);
		
		OrderShipping orderShipping = new OrderShipping();
		order.setShipping(orderShipping);
		
		List<OrderProductItem> ordItemlist =new ArrayList<OrderProductItem>();
		order.setProductItems(ordItemlist);
		
		List<OrderExtraCharge> ordExtraChargelist =new ArrayList<OrderExtraCharge>();
		order.setExtraCharges(ordExtraChargelist);
		
		ObjectMapper mapper = new ObjectMapper();
		
		ProformaInvoiceInfo piInfo = proformaInvoiceVersion.getInfo();
		if(piInfo != null){
			orderInfo.setContact(mapper.writeValueAsString(piInfo.getContact()));
			orderInfo.setCorporation(mapper.writeValueAsString(piInfo.getCorporation()));
			orderInfo.setCreateDate(new java.sql.Date(now.getTime().getTime()));
			orderInfo.setCurrency(piInfo.getCurrency());
			orderInfo.setCustomer(piInfo.getCustomer());
			orderInfo.setCustPo(proformaInvoiceVersion.getProformaInvoice().getCustPo());
			orderInfo.setDataEntryClerk(piInfo.getDataEntryClerk());
			orderInfo.setDiscount(piInfo.getDiscount());
			orderInfo.setSales(piInfo.getSales());
			orderInfo.setShippingDate(piInfo.getShippingDate());
			orderInfo.setTax(piInfo.getTax());
			orderInfo.setWarranty(piInfo.getWarranty());
			orderInfo.setOrder(order);
		}
		
		ProformaInvoiceShipping piShipping = proformaInvoiceVersion.getShipping();
		if(piShipping != null){
			orderShipping.setFare(piShipping.getFare());
			orderShipping.setInfo(piShipping.getInfo());
			orderShipping.setOrder(order);
			orderShipping.setTax(piShipping.getTax());
						
		}
		
		List<ProformaInvoiceProductItem> piProductItems = proformaInvoiceVersion.getProductItems();		
		if(piProductItems.size()>0){
			for(ProformaInvoiceProductItem piProductItem : piProductItems){
				
				OrderProductItem ordItem = new OrderProductItem();
				ordItem.setCurrency(piProductItem.getCurrency());
				ordItem.setProduct(piProductItem.getProduct());
				ordItem.setNote1(piProductItem.getNote1());
				ordItem.setNote2(piProductItem.getNote2());
				ordItem.setNote3(piProductItem.getNote3());
				ordItem.setOrder(order);
				ordItem.setQuantity(piProductItem.getQuantity());
				ordItem.setUnit(piProductItem.getUnit());
				ordItem.setUnitPrice(piProductItem.getUnitPrice());
				ordItem.setSoldQty(0);
				ordItem.setStatus(OrderItemStatus.NORMAL);
				ordItemlist.add(ordItem);
			}
		}
		
		List<ProformaInvoiceExtraCharge> piExtraCharges = proformaInvoiceVersion.getExtraCharges();		
		if(piExtraCharges.size()>0){
			for(ProformaInvoiceExtraCharge piExtraCharge : piExtraCharges){
				
				OrderExtraCharge ordExtraCharge = new OrderExtraCharge();
				ordExtraCharge.setItemName(piExtraCharge.getItemName());
				ordExtraCharge.setOrder(order);
				ordExtraCharge.setPrice(piExtraCharge.getPrice());
				ordExtraCharge.setTax(piExtraCharge.getTax());
			
				ordExtraChargelist.add(ordExtraCharge);
			}
		}
		
		
		salesOrderRepsitory.save(order);
		proformaInvoiceVersionRepsitory.save(proformaInvoiceVersion);
				
		return order;
		
	}
	
	@Transactional
	public Order updateOrder(Order order,String id) throws DataNotFoundException,OperationNotPermitException{
		
		Order dbOrder = salesOrderRepsitory.findOne(id);
		
		if(dbOrder==null){
			throw new DataNotFoundException("can not find Order");
		}
		
		if(!OrderStatus.PROCESSING.equals(dbOrder.getStatus())){
			throw new OperationNotPermitException("Order Status is illegal");
		}
		
		OrderInfo dbOrdInfo = dbOrder.getInfo();	
		OrderInfo ordInfo = order.getInfo();		
		dbOrdInfo.setContact(ordInfo.getContact());
		dbOrdInfo.setCorporation(ordInfo.getCorporation());
		dbOrdInfo.setCurrency(ordInfo.getCurrency());
		dbOrdInfo.setCustomer(ordInfo.getCustomer());
		dbOrdInfo.setDataEntryClerk(ordInfo.getDataEntryClerk());
		dbOrdInfo.setDiscount(ordInfo.getDiscount());
		dbOrdInfo.setSales(ordInfo.getSales());
		dbOrdInfo.setShippingDate(ordInfo.getShippingDate());
		dbOrdInfo.setTax(ordInfo.getTax());
		dbOrdInfo.setWarranty(ordInfo.getWarranty());
		
						
		//dbOrder.setOrderDate(order.getOrderDate());
		
		orderProductItemRepsitory.delete(dbOrder.getProductItems());
		dbOrder.setProductItems(order.getProductItems());
		if(dbOrder.getProductItems()!=null||!dbOrder.getProductItems().isEmpty()){
			for(OrderProductItem productItem:dbOrder.getProductItems()){
				productItem.setOrder(dbOrder);
				productItem.setSoldQty(0);
				productItem.setStatus(OrderItemStatus.NORMAL);
			}
		}
		
		
		OrderShipping dbShipping = dbOrder.getShipping();
		OrderShipping ordShipping = order.getShipping();		
		dbShipping.setFare(ordShipping.getFare());
		dbShipping.setInfo(ordShipping.getInfo());
		dbShipping.setTax(ordShipping.getTax());

		
		orderExtraChargeRepsitory.delete(dbOrder.getExtraCharges());
		dbOrder.setExtraCharges(order.getExtraCharges());
		if(dbOrder.getExtraCharges()!=null||!dbOrder.getExtraCharges().isEmpty()){
			for(OrderExtraCharge ordExtraCharge:dbOrder.getExtraCharges()){
				ordExtraCharge.setOrder(dbOrder);
			}
		}
		
		//dbOrder.setCanceled(order.isCanceled());
		
		salesOrderRepsitory.save(dbOrder);
		
		return dbOrder;
	}
	
	@Transactional
	public Order setOrderStatus(String orderId,SetStatusModel model) throws DataNotFoundException{
		
		Order order = salesOrderRepsitory.findOne(orderId);
		
		if(order == null){
			throw new DataNotFoundException("can not find sales order by id");
		}
				
		order.setStatus(OrderStatus.valueOf(model.getStatus()));		
		salesOrderRepsitory.save(order);
		return order;
	}
	
}
