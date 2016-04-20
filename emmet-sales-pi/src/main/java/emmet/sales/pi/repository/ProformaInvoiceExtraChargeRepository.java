package emmet.sales.pi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.sales.entity.pi.ProformaInvoiceExtraCharge;

@RepositoryRestResource(exported=false)
public interface ProformaInvoiceExtraChargeRepository extends JpaRepository<ProformaInvoiceExtraCharge, String> {



}
