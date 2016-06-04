package emmet.sales.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.Customer;
import emmet.core.data.entity.OrderProductItem;

@RepositoryRestResource(exported=false)
public interface OrderProductItemRepsitory extends PagingAndSortingRepository<OrderProductItem, Integer> {

	
	@Query("select orderItem from OrderProductItem as orderItem "
			+ " left join orderItem.order as _order "
			+ " left join _order.info as info "
			+ " left join info.customer as customer "
			+ " where _order.id like %:ordId% "
			+ " and orderItem.status = 'NORMAL' "
			+ " and _order.status = 'CONFIRMED'"
			+ " and customer.id = :custId ")
	public List<OrderProductItem> findNormalOrderItemByCustAndOrdId(@Param("custId") String custId,@Param("ordId") String ordId); 
	
 
	@Query("select distinct info.customer.id from OrderProductItem as ordItem "
			+ " join ordItem.order as _order "
			+ " join _order.info as info "
			+ " where ordItem.status='NORMAL' "
			+ " and _order.status='CONFIRMED' "
			+ " order by info.customer.id"
			)
	public List<String> findSalesSlipCustList();
	
}
