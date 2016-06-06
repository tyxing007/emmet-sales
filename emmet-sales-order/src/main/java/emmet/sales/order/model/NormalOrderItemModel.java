package emmet.sales.order.model;

import java.math.BigDecimal;

import emmet.core.data.entity.OrderProductItem;

public class NormalOrderItemModel {
	
	private OrderProductItem orderProductItem;
	
	private BigDecimal availableStock;

	public OrderProductItem getOrderProductItem() {
		return orderProductItem;
	}

	public void setOrderProductItem(OrderProductItem orderProductItem) {
		this.orderProductItem = orderProductItem;
	}

	public BigDecimal getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(BigDecimal availableStock) {
		this.availableStock = availableStock;
	}
	
	
	
	

}
