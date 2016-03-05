package emmet.sales.pi.domain;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import emmet.sales.entity.pi.ProformaInvoice;

public class ProformaInvoiceResourceAssembler
		extends ResourceAssemblerSupport<ProformaInvoice, ProformaInvoiceResource> {

	public ProformaInvoiceResourceAssembler(Class<?> controllerClass, Class<ProformaInvoiceResource> resourceType) {
		super(controllerClass, resourceType);
	}

	@Override
	public ProformaInvoiceResource toResource(ProformaInvoice entity) {

		ProformaInvoiceResource resource = createResourceWithId(entity.getId(), entity);

		resource.setInvoiceId(entity.getId());
		resource.setConfirmed(entity.getConfirmed());
		resource.setFinalVersion(entity.getFinalVersion());
		resource.add(new Link(resource.getId().getHref()+"/versions").withRel("versions"));

		return resource;


	}

}
