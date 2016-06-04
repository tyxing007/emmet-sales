package emmet.sales.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.Customer;

@RepositoryRestResource(exported=false)
public interface CustomerRepository extends PagingAndSortingRepository<Customer, String> {


}
