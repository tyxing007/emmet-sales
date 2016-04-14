package emmet.sales.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.Order;

@RepositoryRestResource
public interface SalesOrderRepsitory extends PagingAndSortingRepository<Order, String> {


}