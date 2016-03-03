package emmet.sales.entity.pi;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import emmet.common.service.entity.Currency;
import emmet.common.service.entity.TaxType;
import emmet.core.data.entity.Customer;
import emmet.core.data.entity.Employee;
import emmet.partner.entity.PartnerCorporation;

@Entity
@Table(name = "sales_proforma_invoice_info", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"proforma_invoice_version_id"}) })

public class ProformaInvoiceInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;


	@OneToOne
	@JoinColumn(name = "proforma_invoice_version_id")
	private ProformaInvoiceVersion version;

	private String customerDocumentId;
	private Date createDate;
	private Date proformaInvoiceDate;

	private String customerFormalName;
	private String customerCommonName;

	private String contactName;

	private Date shippingDate;

	@ManyToOne(cascade=CascadeType.ALL)
	private Employee sales;

	@ManyToOne
	private Employee dataEntryClerk;

	@ManyToOne
	private Customer customer;

	@ManyToOne
	private PartnerCorporation corporation;

	private Integer warrantyYear;

	@ManyToOne
	private Currency currency;

	@ManyToOne
	private TaxType taxType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getCustomerDocumentId() {
		return customerDocumentId;
	}

	@JsonIgnore
	public ProformaInvoiceVersion getVersion() {
		return version;
	}

	public void setVersion(ProformaInvoiceVersion version) {
		this.version = version;
	}

	public void setCustomerDocumentId(String customerDocumentId) {
		this.customerDocumentId = customerDocumentId;
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

	public String getCustomerFormalName() {
		return customerFormalName;
	}

	public void setCustomerFormalName(String customerFormalName) {
		this.customerFormalName = customerFormalName;
	}

	public String getCustomerCommonName() {
		return customerCommonName;
	}

	public void setCustomerCommonName(String customerCommonName) {
		this.customerCommonName = customerCommonName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Date getShippingDate() {
		return shippingDate;
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

}
