package emmet.sales.pi.domain;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

import emmet.sales.entity.pi.ProformaInvoiceVersion;

public class ProformaInvoiceResource extends ResourceSupport {

	@JsonProperty("id")
	private String invoiceId;
	private ProformaInvoiceVersion finalVersion;
	private Boolean confirmed;

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public ProformaInvoiceVersion getFinalVersion() {
		return finalVersion;
	}

	public void setFinalVersion(ProformaInvoiceVersion finalVersion) {
		this.finalVersion = finalVersion;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

}
