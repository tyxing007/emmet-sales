package emmet.sales.order.model;

import java.util.List;

import emmet.core.data.entity.OrderProductItem;

public class CreateSalesSlipModel {

	private String userId;
	private List<OrderProductItem> orderItemList;
	private String warehouseId;

	public String getUserId() {
		if (userId == null) {
			userId = "";
		}
		return userId;
	}

	public void setUserId(String userId) {

		this.userId = userId;
	}

	public List<OrderProductItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderProductItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public String getWarehouseId() {
		if (warehouseId == null) {
			warehouseId = "";
		}
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

}
