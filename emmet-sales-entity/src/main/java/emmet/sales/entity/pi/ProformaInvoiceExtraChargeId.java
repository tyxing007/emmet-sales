package emmet.sales.entity.pi;

import java.io.Serializable;

import javax.persistence.Embeddable;
@Embeddable
public class ProformaInvoiceExtraChargeId implements Serializable {
	private static final long serialVersionUID = 1L;
	private String profromaInvocieId;
	private String itemName;

	public String getProfromaInvocieId() {
		return profromaInvocieId;
	}

	public void setProfromaInvocieId(String profromaInvocieId) {
		this.profromaInvocieId = profromaInvocieId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
