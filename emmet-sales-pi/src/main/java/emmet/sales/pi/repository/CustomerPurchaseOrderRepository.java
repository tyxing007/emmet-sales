package emmet.sales.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.CustomerPurchaseOrder;

@RepositoryRestResource(exported=false)
public interface CustomerPurchaseOrderRepository extends JpaRepository<CustomerPurchaseOrder, Integer> {

	public Long countByPoNo(String poNo);

}
