package emmet.sales.pi.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import emmet.sales.entity.pi.ProformaInvoice;
import emmet.sales.entity.pi.ProformaInvoiceExtraCharge;
import emmet.sales.entity.pi.ProformaInvoiceProductItem;
import emmet.sales.entity.pi.ProformaInvoiceVersion;

@Component
public class ProformaInvoiceModelFactory {
	/*
	 * public ProformaInvoice createEntity(ProformaInvoiceModel model) {
	 * 
	 * ProformaInvoice invoiceEntity = new ProformaInvoice();
	 * invoiceEntity.setId(model.getId());
	 * 
	 * // Invoice Info
	 * 
	 * 
	 * 
	 * // Extra Charge List<ProformaInvoiceExtraCharge> extraChagres =
	 * model.getExtraCharges(); if (extraChagres != null &&
	 * !extraChagres.isEmpty()) { invoiceEntity.setExtraCharges(new
	 * ArrayList<ProformaInvoiceExtraCharge>());
	 * 
	 * for (ProformaInvoiceExtraCharge extraCharge : extraChagres) {
	 * extraCharge.setProformaInvoice(invoiceEntity);
	 * invoiceEntity.getExtraCharges().add(extraCharge); } }
	 * 
	 * return invoiceEntity; }
	 * 
	 * 
	 * 
	 * public ProformaInvoiceModel createModel(ProformaInvoice entity) { return
	 * null; }
	 */

	public ProformaInvoiceVersion linkAssociationEntities(ProformaInvoiceVersion invoiceVersionEntity) {

		// Invoice Info
		if (invoiceVersionEntity.getInfo() != null) {
			invoiceVersionEntity.getInfo().setVersion(invoiceVersionEntity);
		}

		// Extra Charge
		List<ProformaInvoiceExtraCharge> extraChagres = invoiceVersionEntity.getExtraCharges();
		if (extraChagres != null && !extraChagres.isEmpty()) {

			for (ProformaInvoiceExtraCharge extraCharge : extraChagres) {
				extraCharge.setVersion(invoiceVersionEntity);
			}
		}

		// ProductItems
		List<ProformaInvoiceProductItem> productItems = invoiceVersionEntity.getProductItems();
		if (productItems != null && !productItems.isEmpty()) {

			for (ProformaInvoiceProductItem productItem : productItems) {
				productItem.setVersion(invoiceVersionEntity);
			}
		}

		return invoiceVersionEntity;

	}
}
