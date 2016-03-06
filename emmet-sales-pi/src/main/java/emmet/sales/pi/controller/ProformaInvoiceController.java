package emmet.sales.pi.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import emmet.sales.entity.pi.ProformaInvoice;
import emmet.sales.entity.pi.ProformaInvoiceVersion;
import emmet.sales.pi.domain.ProformaInvoiceResource;
import emmet.sales.pi.domain.ProformaInvoiceResourceAssembler;
import emmet.sales.pi.exception.OperationNotPermitException;
import emmet.sales.pi.repository.ProformaInvoiceRepsitory;
import emmet.sales.pi.service.ProformaInvoiceService;

@RepositoryRestController()
@RequestMapping("/proformaInvoices")
public class ProformaInvoiceController {

	@Autowired
	ProformaInvoiceService proformaInvoiceService;

	@Autowired
	ProformaInvoiceRepsitory proformaInvoiceRepsitory;

	ProformaInvoiceResourceAssembler proformaInvoiceResourceAssembler;

	@PostConstruct
	public void init() {
		proformaInvoiceResourceAssembler = new ProformaInvoiceResourceAssembler(ProformaInvoiceController.class,
				ProformaInvoiceResource.class);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getProformaInvoices(Pageable page) {
		
		Page<ProformaInvoice> proformaInvoicePage = proformaInvoiceRepsitory.findAll(page);
		
		List<ProformaInvoiceResource> resources = proformaInvoiceResourceAssembler.toResources(proformaInvoicePage.getContent());
		  Page<ProformaInvoiceResource> resultPage = new
		  PageImpl<ProformaInvoiceResource>(resources, page,
		  proformaInvoicePage.getTotalElements());

		return ResponseEntity.ok(resultPage);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ProformaInvoiceResource> getProformaInvoice(@PathVariable String id) {
		ProformaInvoice proformaInvoice = proformaInvoiceRepsitory.findOne(id);

		ProformaInvoiceResource resource = proformaInvoiceResourceAssembler.toResource(proformaInvoice);
		return ResponseEntity.ok(resource);

	}

	@RequestMapping(value = "/search/findBySales", method = RequestMethod.GET)
	public ResponseEntity<?> findProformaInvoiceBySels(@RequestParam("id") String id, Pageable page) {

		Page<ProformaInvoice> proformaInvoicePage = proformaInvoiceRepsitory.findBySales(id, page);
		
		List<ProformaInvoiceResource> resources = proformaInvoiceResourceAssembler.toResources(proformaInvoicePage.getContent());
		  Page<ProformaInvoiceResource> resultPage = new
		  PageImpl<ProformaInvoiceResource>(resources, page,
		  proformaInvoicePage.getTotalElements());

		return ResponseEntity.ok(resultPage);

	}

	@RequestMapping(value = "/{id}/versions", method = RequestMethod.GET)
	public ResponseEntity<?> getProformaInvoiceVersions(@PathVariable String id) {
		ProformaInvoice proformaInvoice = proformaInvoiceRepsitory.findOne(id);
		return ResponseEntity.ok(proformaInvoice.getVersions());

	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveProformaInvoice(@RequestBody ProformaInvoiceVersion invoice) {

		return new ResponseEntity<ProformaInvoice>(proformaInvoiceService.createProformaInvoice(invoice),
				HttpStatus.CREATED);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateProformaInvoice(@PathVariable String id,
			@RequestBody ProformaInvoiceVersion invoice) {

		ProformaInvoice proformaInvoice = proformaInvoiceRepsitory.findOne(id);
		invoice.setProformaInvoice(proformaInvoice);

		try {

			return new ResponseEntity<ProformaInvoice>(proformaInvoiceService.updateProformaInvoice(invoice),
					HttpStatus.CREATED);

		} catch (OperationNotPermitException e) {

			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

	}
	
	@RequestMapping(value = "/{id}/finalVersion", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<?> setFinalVersion(String versionId){
		return null;
		
	}
	
	
}
