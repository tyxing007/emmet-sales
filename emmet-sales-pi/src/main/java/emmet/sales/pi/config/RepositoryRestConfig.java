package emmet.sales.pi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import emmet.common.service.entity.Currency;
import emmet.core.data.entity.Product;
import emmet.sales.entity.pi.ProformaInvoice;
import emmet.sales.entity.pi.ProformaInvoiceVersion;

@Configuration
public class RepositoryRestConfig extends RepositoryRestMvcConfiguration {
	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(ProformaInvoice.class);
		config.exposeIdsFor(Product.class);
		config.exposeIdsFor(Currency.class);
		config.exposeIdsFor(ProformaInvoiceVersion.class);
		config.setReturnBodyOnCreate(true);
	}
}
