package emmet.sales.order.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emmet.core.data.entity.BatchNumber;
import emmet.core.data.entity.Material;
import emmet.core.data.entity.MaterialStock;
import emmet.core.data.entity.Warehouse;
import emmet.sales.order.model.MaterialWarehouseStockModel;

@RepositoryRestResource(exported = false)
public interface MaterialStockRepository extends PagingAndSortingRepository<MaterialStock, Long>{
	
	@Query(value = "SELECT sum(ms.ioQty) FROM MaterialStock ms WHERE ms.material = :material "
			+ " and ms.batchNumber = :batchNumber "
			+ " and ms.warehouse = :warehouse "
			+ " and ms.enabled=true "
			+ " group by ms.material,ms.batchNumber,ms.warehouse")
    public BigDecimal getWareHouseProductStockQtyWithBatchNo(@Param("material") Material material,
    		@Param("warehouse")Warehouse warehouse,@Param("batchNumber")BatchNumber batchNumber);
	

	@Query(value = "SELECT sum(ms.ioQty) FROM MaterialStock ms WHERE ms.material = :material "
			+ " and ms.batchNumber is null "
			+ " and ms.warehouse = :warehouse "
			+ " and ms.enabled=true "
			+ " group by ms.material,ms.batchNumber,ms.warehouse")
    public BigDecimal getWareHouseProductStockNoBatchNo(@Param("material") Material material,
    		@Param("warehouse")Warehouse warehouse);
	
	
	@Query(value = "SELECT coalesce(sum(ms.ioQty),0) FROM MaterialStock ms "
			+ "join ms.warehouse wh "
			+ " WHERE ms.material = :material "
			+ " and ms.batchNumber = :batchNumber "
			+ " and wh.isAvailable = true "
			+ " and ms.enabled = true "
			)
    public BigDecimal getAvailableStockWithBatchNo(@Param("material") Material material,
    		@Param("batchNumber")BatchNumber batchNumber);
	
	
	@Query(value = "SELECT coalesce(sum(ms.ioQty),0) FROM MaterialStock ms "
			+ "join ms.warehouse wh "
			+ " WHERE ms.material = :material "
			+ " and wh.isAvailable = true "
			+ " and ms.enabled = true "
			)
    public BigDecimal getAvailableStockNoBatchNo(@Param("material") Material material);
	
	
	
	
	@Query(value = "SELECT new emmet.sales.order.model.MaterialWarehouseStockModel(ms.warehouse,coalesce(sum(ms.ioQty),0)) "
			+ " from MaterialStock ms "
			+ " join ms.warehouse wh "
			+ " WHERE ms.material = :material "
			+ " and wh.isAvailable = true "
			+ " and ms.batchNumber = :batchNumber "
			+ " and ms.enabled = true "
			+ " group by ms.warehouse" 
			+ " having coalesce(sum(ms.ioQty),0) > 0 "
		    + " order by coalesce(sum(ms.ioQty),0) asc"
			)
	public List<MaterialWarehouseStockModel> getMaterialStockList(@Param("material") Material material,
			@Param("batchNumber")BatchNumber batchNumber);
	
}
