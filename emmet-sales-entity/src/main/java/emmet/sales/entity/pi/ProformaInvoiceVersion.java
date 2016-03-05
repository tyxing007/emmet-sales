package emmet.sales.entity.pi;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sales_proforma_invoice_versions", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "proforma_invoice_id", "versionSequence" }) })
public class ProformaInvoiceVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@ManyToOne
	@JoinColumn(name = "proforma_invoice_id", nullable = false)
	private ProformaInvoice proformaInvoice;


	private Integer versionSequence;


	private Calendar createDateTime;

	@OneToOne(mappedBy = "version", cascade = CascadeType.ALL)
	private ProformaInvoiceInfo info;

	@OneToMany(mappedBy = "version", cascade = CascadeType.ALL)
	private List<ProformaInvoiceExtraCharge> extraCharges;

	@OneToOne(mappedBy = "version", cascade = CascadeType.ALL)
	private ProformaInvoiceShipping shipping;

	@OneToMany(mappedBy = "version", cascade = CascadeType.ALL)
	private List<ProformaInvoiceProductItem> productItems;

	@Column(length = 2048)
	private String snapshot;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	public ProformaInvoice getProformaInvoice() {
		return proformaInvoice;
	}

	public void setProformaInvoice(ProformaInvoice proformaInvoice) {
		this.proformaInvoice = proformaInvoice;
	}

	public Integer getVersionSequence() {
		return versionSequence;
	}

	public void setVersionSequence(Integer versionSequence) {
		this.versionSequence = versionSequence;
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

	public Calendar getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Calendar createDateTime) {
		this.createDateTime = createDateTime;
	}

	@JsonIgnore
	public String getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
	}

}
