package emmet.sales.pi.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.Product;

@RepositoryRestResource(exported=true)
public interface ProductRepsitory extends PagingAndSortingRepository<Product, String> {

}
