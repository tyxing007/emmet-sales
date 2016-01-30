package emmet.sales.pi.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import emmet.sales.entity.pi.ProformaInvoice;

public interface ProformaInvoiceRepsitory extends PagingAndSortingRepository<ProformaInvoice, String> {

}
