package emmet.sales.entity.pi;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import emmet.common.service.entity.Currency;
import emmet.common.service.entity.TaxType;
import emmet.core.data.entity.Partner;
import emmet.partner.entity.PartnerContact;

@Entity
@Table(name = "sales_pi")
public class ProformaInvoice  implements Serializable{
	private static final long serialVersionUID = 3100380561005523630L;
	@Id
	private String id;
	private String customerDocumentId;
	private Date date;

	@OneToOne
	private ProformaInvoiceAuthorization authoziation;

	@OneToOne
	private Partner customer;

	@OneToOne
	private PartnerContact contact;

	@OneToMany(mappedBy = "proformaInvoice")
	private List<PiProductItem> productItems;

	private Integer warrantyYear;

	@OneToOne
	private Currency currency;

	@OneToOne
	private TaxType taxType;

	@OneToOne
	private PartnerShipment shipment;

	private BigDecimal shipmentFee;
	private BigDecimal extraFee;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerDocumentId() {
		return customerDocumentId;
	}

	public void setCustomerDocumentId(String customerDocumentId) {
		this.customerDocumentId = customerDocumentId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ProformaInvoiceAuthorization getAuthoziation() {
		return authoziation;
	}

	public void setAuthoziation(ProformaInvoiceAuthorization authoziation) {
		this.authoziation = authoziation;
	}

	public Partner getCustomer() {
		return customer;
	}

	public void setCustomer(Partner customer) {
		this.customer = customer;
	}

	public PartnerContact getContact() {
		return contact;
	}

	public void setContact(PartnerContact contact) {
		this.contact = contact;
	}

	public List<PiProductItem> getProductItems() {
		return productItems;
	}

	public void setProductItems(List<PiProductItem> productItems) {
		this.productItems = productItems;
	}

	public Integer getWarrantyYear() {
		return warrantyYear;
	}

	public void setWarrantyYear(Integer warrantyYear) {
		this.warrantyYear = warrantyYear;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public TaxType getTaxType() {
		return taxType;
	}

	public void setTaxType(TaxType taxType) {
		this.taxType = taxType;
	}

	public PartnerShipment getShipment() {
		return shipment;
	}

	public void setShipment(PartnerShipment shipment) {
		this.shipment = shipment;
	}

	public BigDecimal getShipmentFee() {
		return shipmentFee;
	}

	public void setShipmentFee(BigDecimal shipmentFee) {
		this.shipmentFee = shipmentFee;
	}

	public BigDecimal getExtraFee() {
		return extraFee;
	}

	public void setExtraFee(BigDecimal extraFee) {
		this.extraFee = extraFee;
	}

}
