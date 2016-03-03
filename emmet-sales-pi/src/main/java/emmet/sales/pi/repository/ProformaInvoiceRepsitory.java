package emmet.sales.pi.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.sales.entity.pi.ProformaInvoice;

@RepositoryRestResource
public interface ProformaInvoiceRepsitory extends PagingAndSortingRepository<ProformaInvoice, String> {

}
