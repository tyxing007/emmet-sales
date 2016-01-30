package emmet.sales.entity.pi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import emmet.core.data.entity.Partner;

@Entity
@Table(name = "partner_shipment")
public class PartnerShipment {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;

	@ManyToOne
	private Partner partner;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonIgnore
	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

}
