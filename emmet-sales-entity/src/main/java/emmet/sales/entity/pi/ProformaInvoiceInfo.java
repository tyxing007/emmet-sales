package emmet.sales.entity.pi;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import emmet.core.data.entity.Currency;
import emmet.core.data.entity.Customer;
import emmet.core.data.entity.Employee;
import emmet.partner.entity.PartnerContact;
import emmet.partner.entity.PartnerCorporation;

@Entity
@Table(name = "sales_proforma_invoice_info", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "proforma_invoice_version_id" }) })

public class ProformaInvoiceInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "proforma_invoice_version_id")
	private ProformaInvoiceVersion version;

	
	private Date createDate;
	private Date proformaInvoiceDate;

	@OneToOne
	private PartnerContact contact;

	private Date shippingDate;

	@OneToOne
	private Employee sales;

	@OneToOne
	private Employee dataEntryClerk;

	@OneToOne
	private Customer customer;

	@OneToOne
	private PartnerCorporation corporation;

	private String warranty;

	@OneToOne
	private Currency currency;

	private String tax;

	private BigDecimal discount;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	@JsonIgnore
	public ProformaInvoiceVersion getVersion() {
		return version;
	}

	public void setVersion(ProformaInvoiceVersion version) {
		this.version = version;
	}

	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getProformaInvoiceDate() {
		return proformaInvoiceDate;
	}

	public void setProformaInvoiceDate(Date proformaInvoiceDate) {
		this.proformaInvoiceDate = proformaInvoiceDate;
	}

	public Date getShippingDate() {
		return shippingDate;
	}

	public PartnerContact getContact() {
		return contact;
	}

	public void setContact(PartnerContact contact) {
		this.contact = contact;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	public Employee getSales() {
		return sales;
	}

	public void setSales(Employee sales) {
		this.sales = sales;
	}

	public Employee getDataEntryClerk() {
		return dataEntryClerk;
	}

	public void setDataEntryClerk(Employee dataEntryClerk) {
		this.dataEntryClerk = dataEntryClerk;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public PartnerCorporation getCorporation() {
		return corporation;
	}

	public void setCorporation(PartnerCorporation corporation) {
		this.corporation = corporation;
	}

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	
}
