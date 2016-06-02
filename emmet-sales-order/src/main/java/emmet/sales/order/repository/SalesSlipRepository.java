package emmet.sales.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.SalesSlip;

@RepositoryRestResource(exported=false)
public interface SalesSlipRepository extends PagingAndSortingRepository<SalesSlip, String> {


}
