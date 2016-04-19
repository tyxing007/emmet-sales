package emmet.sales.pi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.sales.entity.pi.ProformaInvoice;

@RepositoryRestResource
public interface ProformaInvoiceRepsitory extends PagingAndSortingRepository<ProformaInvoice, String> {

	@Query("SELECT pi FROM ProformaInvoice pi where pi.id in "
			+ "("
			+ "select v.proformaInvoice.id from ProformaInvoiceVersion v "
			+ "where v.proformaInvoice.id like %:piId% "
			+ "and v.info.sales.id = :salesId group by v.proformaInvoice.id"
			+ ") order by pi.id desc")
	Page<ProformaInvoice> findBySales(@Param("piId") String piId, @Param("salesId") String salesId, Pageable page);


}
