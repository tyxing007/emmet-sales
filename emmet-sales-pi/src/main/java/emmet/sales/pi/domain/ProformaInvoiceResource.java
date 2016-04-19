package emmet.sales.pi.domain;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProformaInvoiceResource extends ResourceSupport {

	@JsonProperty("id")
	private String invoiceId;

	
	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}



	
	


}
