package emmet.sales.order.bo;

import java.math.BigDecimal;

import emmet.core.data.entity.SalesSlipDetail;

public class SalesSlipDetailBo  {

	private SalesSlipDetail salesSlipDetail;
	private BigDecimal availableStock;

	public BigDecimal getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(BigDecimal availableStock) {
		this.availableStock = availableStock;
	}

	public SalesSlipDetail getSalesSlipDetail() {
		return salesSlipDetail;
	}

	public void setSalesSlipDetail(SalesSlipDetail salesSlipDetail) {
		this.salesSlipDetail = salesSlipDetail;
	}
	
	
}
