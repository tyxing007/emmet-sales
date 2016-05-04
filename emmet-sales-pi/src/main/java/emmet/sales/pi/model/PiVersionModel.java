package emmet.sales.pi.model;

import emmet.core.data.entity.CustomerPurchaseOrder;
import emmet.sales.entity.pi.ProformaInvoiceVersion;

public class PiVersionModel {
	
	private CustomerPurchaseOrder custPo;
	private ProformaInvoiceVersion proformaInvoiceVersion;
	public CustomerPurchaseOrder getCustPo() {
		return custPo;
	}
	public void setCustPo(CustomerPurchaseOrder custPo) {
		this.custPo = custPo;
	}
	public ProformaInvoiceVersion getProformaInvoiceVersion() {
		return proformaInvoiceVersion;
	}
	public void setProformaInvoiceVersion(
			ProformaInvoiceVersion proformaInvoiceVersion) {
		this.proformaInvoiceVersion = proformaInvoiceVersion;
	}
	
	
	
}
