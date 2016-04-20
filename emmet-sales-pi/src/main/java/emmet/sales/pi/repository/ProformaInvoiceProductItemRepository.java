package emmet.sales.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.sales.entity.pi.ProformaInvoiceProductItem;

@RepositoryRestResource(exported=false)
public interface ProformaInvoiceProductItemRepository extends JpaRepository<ProformaInvoiceProductItem, String> {

}
