package emmet.sales.order.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.sales.entity.pi.ProformaInvoiceVersion;

@RepositoryRestResource(exported=false)
public interface ProformaInvoiceVersionRepsitory extends PagingAndSortingRepository<ProformaInvoiceVersion, String> {
	
	List<ProformaInvoiceVersion> findByProformaInvoiceIdAndOrderIsNotNull(String id);

}
