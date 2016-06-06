package emmet.sales.order.model;

import java.math.BigDecimal;

import emmet.core.data.entity.Warehouse;

public class MaterialWarehouseStockModel {

	private BigDecimal stock;
	private Warehouse warehouse;
	
	public MaterialWarehouseStockModel(Warehouse warehouse,BigDecimal stock){
		this.stock=stock;
		this.warehouse=warehouse;
	}

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	
	
	
}
