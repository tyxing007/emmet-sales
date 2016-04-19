package emmet.sales.entity.pi;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

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


	public enum ProformainvoiceStatus {
		
		INITIALIZED("INITIALIZED"),PROCESSING("PROCESSING"),CONFIRMED("CONFIRMED"),
		ABANDONED("ABANDONED"),ORDERED("ORDERED");

		private String name;
		
		private ProformainvoiceStatus(String name){
			this.name=name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		
	}

}
