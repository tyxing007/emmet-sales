package emmet.sales.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.Warehouse;

@RepositoryRestResource(exported=false)
public interface WarehouseRepository extends PagingAndSortingRepository<Warehouse, String> {


}
