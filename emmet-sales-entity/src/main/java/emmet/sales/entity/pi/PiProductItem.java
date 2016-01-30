package emmet.sales.entity.pi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import emmet.core.data.entity.Product;

@Entity
@Table(name = "sales_pi_products")
public class PiProductItem implements Serializable {
	private static final long serialVersionUID = 6005298777381603865L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;

	@ManyToOne()
	@JoinColumn(name = "proformaInvoice")
	private ProformaInvoice proformaInvoice;

	@OneToOne()
	private Product product;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonIgnore
	public ProformaInvoice getProformaInvoice() {
		return proformaInvoice;
	}

	public void setProformaInvoice(ProformaInvoice proformaInvoice) {
		this.proformaInvoice = proformaInvoice;
	}


	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
