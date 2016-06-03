package emmet.sales.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.BatchNumber;
import emmet.core.data.entity.Material;

@RepositoryRestResource(exported = false)
public interface BatchNumberRepository extends PagingAndSortingRepository<BatchNumber, Long>{
	
	public BatchNumber findByMaterialAndCode(Material material,String code);
	
}
