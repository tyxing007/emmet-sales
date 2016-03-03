package emmet.sales.pi.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.sales.entity.pi.ProformaInvoiceShipping;

//@RepositoryRestResource(exported=false)
public interface ProformaInvoiceShippingRepsitory extends PagingAndSortingRepository<ProformaInvoiceShipping, Integer> {

}
