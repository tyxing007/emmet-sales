package emmet.sales.pi.model;

import emmet.sales.entity.pi.ProformaInvoice;
import emmet.sales.entity.pi.ProformaInvoiceVersion;

public class ProformaInvoiceModel {
	
	ProformaInvoice proformaInvoice;
	ProformaInvoiceVersion finalVersion;
	Boolean hasOrder;
	
	public ProformaInvoice getProformaInvoice() {
		return proformaInvoice;
	}
	public void setProformaInvoice(ProformaInvoice proformaInvoice) {
		this.proformaInvoice = proformaInvoice;
	}
	public ProformaInvoiceVersion getFinalVersion() {
		return finalVersion;
	}
	public void setFinalVersion(ProformaInvoiceVersion finalVersion) {
		this.finalVersion = finalVersion;
	}
	public Boolean getHasOrder() {
		return hasOrder;
	}
	public void setHasOrder(Boolean hasOrder) {
		this.hasOrder = hasOrder;
	}
	
	

}
