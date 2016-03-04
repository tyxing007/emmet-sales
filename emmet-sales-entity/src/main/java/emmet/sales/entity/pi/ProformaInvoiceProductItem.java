package emmet.sales.entity.pi;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import emmet.common.service.entity.Currency;
import emmet.core.data.entity.Product;

@Entity
@Table(name = "sales_proforma_invoice_products")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProformaInvoiceProductItem implements Serializable {
	private static final long serialVersionUID = 1L;;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "proforma_invoice_version_id")
	private ProformaInvoiceVersion version;

	@OneToOne
	private Product product;
	private Integer quantity;
	private String unit;
	private BigDecimal unitPrice;
	private String note1;
	private String note2;
	private String note3;

	@OneToOne
	private Currency currency;

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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getNote1() {
		return note1;
	}

	public void setNote1(String note1) {
		this.note1 = note1;
	}

	public String getNote2() {
		return note2;
	}

	public void setNote2(String note2) {
		this.note2 = note2;
	}

	public String getNote3() {
		return note3;
	}

	public void setNote3(String note3) {
		this.note3 = note3;
	}
	
	

}
