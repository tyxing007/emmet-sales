package emmet.sales.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.Employee;

@RepositoryRestResource(exported=false)
public interface EMployeeRepository extends PagingAndSortingRepository<Employee, String> {


}
