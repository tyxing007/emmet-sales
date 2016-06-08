package emmet.sales.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.SalesSlipDetail;

@RepositoryRestResource(exported=false)
public interface SalesSlipDetailRepository extends PagingAndSortingRepository<SalesSlipDetail, String> {


}
