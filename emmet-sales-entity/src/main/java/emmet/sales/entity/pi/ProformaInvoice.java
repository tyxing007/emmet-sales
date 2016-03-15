package emmet.sales.entity.pi;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "sales_proforma_invoice")
public class ProformaInvoice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "emmet.sales.entity.pi.DatePrifixIdGenerator")
	private String id;

	@OneToMany(mappedBy = "proformaInvoice", cascade = CascadeType.ALL)
	private List<ProformaInvoiceVersion> versions;

	@OneToOne
	private ProformaInvoiceVersion finalVersion;

	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	public List<ProformaInvoiceVersion> getVersions() {
		return versions;
	}

	public void setVersions(List<ProformaInvoiceVersion> versions) {
		this.versions = versions;
	}

	public ProformaInvoiceVersion getFinalVersion() {
		return finalVersion;
	}

	public void setFinalVersion(ProformaInvoiceVersion finalVersion) {
		this.finalVersion = finalVersion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public class ProformainvoiceStatus {

		public static final String PROCESSING = "PROCESSING";
		public static final String CONFIRMED = "CONFIRMED";
		public static final String ABANDONED = "ABANDONED";
	}

}
