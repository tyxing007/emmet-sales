package emmet.sales.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.Order;

@RepositoryRestResource(exported=false)
public interface SalesOrderRepsitory extends PagingAndSortingRepository<Order, String> {

	@Query("select o from Order o where o.id like %:id%")
	Page<Order> findByIdLike(@Param("id") String id,Pageable page);

}
