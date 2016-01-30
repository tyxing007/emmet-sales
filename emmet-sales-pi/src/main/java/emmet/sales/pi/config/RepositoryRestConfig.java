package emmet.sales.pi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import emmet.core.data.entity.AccountPayable;
import emmet.core.data.entity.AccountReceivable;
import emmet.core.data.entity.Bom;
import emmet.core.data.entity.Customer;
import emmet.core.data.entity.Department;
import emmet.core.data.entity.Employee;
import emmet.core.data.entity.Inventory;
import emmet.core.data.entity.Material;
import emmet.core.data.entity.MaterialCost;
import emmet.core.data.entity.Order;
import emmet.core.data.entity.Part;
import emmet.core.data.entity.PartMaterial;
import emmet.core.data.entity.Partner;
import emmet.core.data.entity.Product;
import emmet.core.data.entity.ProductPrice;
import emmet.core.data.entity.Purchase;
import emmet.core.data.entity.Stock;
import emmet.core.data.entity.StorageSpace;
import emmet.core.data.entity.Supplier;

@Configuration
public class RepositoryRestConfig extends RepositoryRestMvcConfiguration {
	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

		config.exposeIdsFor(Partner.class);
	

	}
}
