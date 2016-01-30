package emmet.sales.entity.pi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sales_pi_auth")
public class ProformaInvoiceAuthorization {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
