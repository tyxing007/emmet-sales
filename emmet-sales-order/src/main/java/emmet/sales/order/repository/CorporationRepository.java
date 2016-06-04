package emmet.sales.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.partner.entity.PartnerCorporation;

@RepositoryRestResource(exported = false)
public interface CorporationRepository extends PagingAndSortingRepository<PartnerCorporation, Long>{
	
	public PartnerCorporation findByPartnerId(String id);
	
}
