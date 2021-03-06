package emmet.sales.pi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.sales.entity.pi.ProformaInvoiceVersion;

@RepositoryRestResource
public interface ProformaInvoiceVersionRepsitory extends JpaRepository<ProformaInvoiceVersion, String> {
	@Query("SELECT MAX(piv.versionSequence) FROM ProformaInvoiceVersion piv WHERE piv.proformaInvoice.id = :pid")
	Integer findLastVersionSequenceOfProformainvoice(@Param("pid") String pid);

	List<ProformaInvoiceVersion> findByProformaInvoiceId(@Param("pid") String pid);
	
	ProformaInvoiceVersion findByProformaInvoiceIdAndId(@Param("invoiceId") String pid, @Param("versionId") String vid);
	
	ProformaInvoiceVersion findByOrderId(String id);
	
	ProformaInvoiceVersion findFirstByProformaInvoiceIdOrderByIdDesc(String id);
	
	@Query("select count(piv) from ProformaInvoiceVersion piv where piv.proformaInvoice.id=:id and piv.order.id is not null")
	Integer findVersionOrderCount(@Param("id") String id );
	
	
}
