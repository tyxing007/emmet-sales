package emmet.sales.pi.domain;

import java.util.List;

import emmet.sales.entity.pi.ProformaInvoiceExtraCharge;
import emmet.sales.entity.pi.ProformaInvoiceInfo;
import emmet.sales.entity.pi.ProformaInvoiceProductItem;
import emmet.sales.entity.pi.ProformaInvoiceShipping;

public class ProformaInvoiceModel {

	private String id;


	private ProformaInvoiceInfo info;

	
	private List<ProformaInvoiceExtraCharge> extraCharges;

	private ProformaInvoiceShipping shipping;


	private List<ProformaInvoiceProductItem> productItems;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProformaInvoiceInfo getInfo() {
		return info;
	}

	public void setInfo(ProformaInvoiceInfo info) {
		this.info = info;
	}

	public List<ProformaInvoiceExtraCharge> getExtraCharges() {
		return extraCharges;
	}

	public void setExtraCharges(List<ProformaInvoiceExtraCharge> extraCharges) {
		this.extraCharges = extraCharges;
	}

	public ProformaInvoiceShipping getShipping() {
		return shipping;
	}

	public void setShipping(ProformaInvoiceShipping shipping) {
		this.shipping = shipping;
	}

	public List<ProformaInvoiceProductItem> getProductItems() {
		return productItems;
	}

	public void setProductItems(List<ProformaInvoiceProductItem> productItems) {
		this.productItems = productItems;
	}


}
