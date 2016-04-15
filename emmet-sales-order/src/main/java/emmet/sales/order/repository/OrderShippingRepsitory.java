package emmet.sales.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.OrderShipping;

@RepositoryRestResource(exported=false)
public interface OrderShippingRepsitory extends PagingAndSortingRepository<OrderShipping, Integer> {


}
