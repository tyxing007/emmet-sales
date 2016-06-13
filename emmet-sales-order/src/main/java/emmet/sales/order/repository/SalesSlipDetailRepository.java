package emmet.sales.order.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.OrderProductItem;
import emmet.core.data.entity.SalesSlipDetail;

@RepositoryRestResource(exported=false)
public interface SalesSlipDetailRepository extends PagingAndSortingRepository<SalesSlipDetail, String> {

	@Query(value = "SELECT coalesce(sum(ms.ioQty),0) FROM SalesSlipDetail ssd "
			+ " join ssd.materialStock ms "
			+ " WHERE ssd.orderItem = :orderItem "
			+ " and ms.enabled = true "
			)
    public BigDecimal getSoldStock(@Param("orderItem") OrderProductItem orderItem);

}
