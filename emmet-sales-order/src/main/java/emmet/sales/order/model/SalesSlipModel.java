package emmet.sales.order.model;

import java.util.List;

import emmet.core.data.entity.SalesSlip;
import emmet.sales.order.bo.SalesSlipDetailBo;

public class SalesSlipModel {

	private SalesSlip salesSlip;
	private List<SalesSlipDetailBo> salesSlipDetails;
	public SalesSlip getSalesSlip() {
		return salesSlip;
	}
	public void setSalesSlip(SalesSlip salesSlip) {
		this.salesSlip = salesSlip;
	}
	public List<SalesSlipDetailBo> getSalesSlipDetails() {
		return salesSlipDetails;
	}
	public void setSalesSlipDetails(List<SalesSlipDetailBo> salesSlipDetails) {
		this.salesSlipDetails = salesSlipDetails;
	}

	
	
	
}
