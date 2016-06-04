package emmet.sales.order.model;

import emmet.core.data.entity.Customer;
import emmet.partner.entity.PartnerCorporation;

public class CustomerModel {

	private Customer customer;
	private PartnerCorporation corporation;
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
	
	
	
}
