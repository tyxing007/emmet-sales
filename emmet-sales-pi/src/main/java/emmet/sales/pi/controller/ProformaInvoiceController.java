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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import emmet.sales.entity.pi.ProformaInvoice;
import emmet.sales.entity.pi.ProformaInvoiceVersion;
import emmet.sales.pi.domain.ProformaInvoiceResource;
import emmet.sales.pi.domain.ProformaInvoiceResourceAssembler;
import emmet.sales.pi.exception.DataNotFoundException;
import emmet.sales.pi.exception.OperationNotPermitException;
import emmet.sales.pi.model.SetStatusModel;
import emmet.sales.pi.repository.ProformaInvoiceRepsitory;
import emmet.sales.pi.repository.ProformaInvoiceVersionRepsitory;
import emmet.sales.pi.service.ProformaInvoiceService;

@RepositoryRestController()
@RequestMapping("/proformaInvoices")
public class ProformaInvoiceController {

	@Autowired
	ProformaInvoiceService proformaInvoiceService;

	@Autowired
	ProformaInvoiceRepsitory proformaInvoiceRepsitory;
	
	@Autowired
	ProformaInvoiceVersionRepsitory proformaInvoiceVersionRepsitory;

	ProformaInvoiceResourceAssembler proformaInvoiceResourceAssembler;

	@PostConstruct
	public void init() {
		proformaInvoiceResourceAssembler = new ProformaInvoiceResourceAssembler(ProformaInvoiceController.class,
				ProformaInvoiceResource.class);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getProformaInvoices(Pageable page) {

		Page<ProformaInvoice> proformaInvoicePage = proformaInvoiceRepsitory.findAll(page);

		List<ProformaInvoiceResource> resources = proformaInvoiceResourceAssembler
				.toResources(proformaInvoicePage.getContent());
		Page<ProformaInvoiceResource> resultPage = new PageImpl<ProformaInvoiceResource>(resources, page,
				proformaInvoicePage.getTotalElements());

		return ResponseEntity.ok(resultPage);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getProformaInvoice(@PathVariable String id) {
		ProformaInvoice proformaInvoice = proformaInvoiceRepsitory.findOne(id);
		if (proformaInvoice == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		ProformaInvoiceResource resource = proformaInvoiceResourceAssembler.toResource(proformaInvoice);
		return ResponseEntity.ok(resource);

	}

	@RequestMapping(value = "/search/findBySales", method = RequestMethod.GET)
	public ResponseEntity<?> findProformaInvoiceBySels(@RequestParam("id") String id, Pageable page) {
		Page<ProformaInvoice> proformaInvoicePage = null;
		
		proformaInvoicePage = proformaInvoiceRepsitory.findBySales(id, page);

		List<ProformaInvoiceResource> resources = proformaInvoiceResourceAssembler
				.toResources(proformaInvoicePage.getContent());
		Page<ProformaInvoiceResource> resultPage = new PageImpl<ProformaInvoiceResource>(resources, page,
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

		try {

			return new ResponseEntity<ProformaInvoice>(proformaInvoiceService.updateProformaInvoice(invoice, id),
					HttpStatus.CREATED);

		} catch (OperationNotPermitException e) {

			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (DataNotFoundException e) {

			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = "/{proformaInvoiceId}/versions/{versionId}", method = RequestMethod.GET)
	public ResponseEntity<?> getVersion(@PathVariable("proformaInvoiceId") String proformaInvoiceId,
			@PathVariable("versionId") String versionId) {
		ProformaInvoiceVersion invoice = null;
		try {
			invoice = proformaInvoiceService.findProformaInvoiceWithVersion(proformaInvoiceId, versionId);

		} catch (DataNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(invoice);

	}

	@Transactional
	@RequestMapping(value = "/{id}/setFinalVersion", method = RequestMethod.PUT)
	public ResponseEntity<?> updateFinalVersion(@PathVariable String id, @RequestParam("version") String versionId) {

		int recordsCount = proformaInvoiceRepsitory.setFinalVersion(id, versionId);
		if (recordsCount == 0) {
			return new ResponseEntity<String>("Set final version fail, the id or version may invalid.",
					HttpStatus.NOT_FOUND);

		} else if (recordsCount != 1) {
			throw new RuntimeException("There are troubles!");
		}

		return ResponseEntity.ok("The final version setted.");

	}

//	@Transactional
//	@RequestMapping(value = "/{id}/setConfirmed", method = RequestMethod.PUT)
//	public ResponseEntity<?> confirm(@PathVariable String id) {
//
//		int recordsCount = proformaInvoiceRepsitory.setConfirmed(id);
//
//		if (recordsCount == 0) {
//			return new ResponseEntity<String>("Can't set confirmed, the id may invalid.", HttpStatus.NOT_FOUND);
//
//		} else if (recordsCount != 1) {
//			throw new RuntimeException("There are troubles!");
//		}
//
//		return ResponseEntity.ok("The proforma invoice is confirmed.");
//	}
	
	@RequestMapping(value = "/versions/search/findByOrder", method = RequestMethod.GET)
	public ResponseEntity<?> findProformaInvoiceByOrderId(@RequestParam("id") String id) {

		
		ProformaInvoiceVersion piVersion = proformaInvoiceVersionRepsitory.findByOrderId(id);
		
		if(piVersion==null){
			return new ResponseEntity<String>("Can't find ProformaInvoiceVersion", HttpStatus.NOT_FOUND);
		}
	

		return ResponseEntity.ok(piVersion);

	}
	
	@RequestMapping(value="/versions/{versionId}/setStatus",method=RequestMethod.PUT)
	public ResponseEntity<?> setProformaInvoiceVersion(@RequestBody SetStatusModel model,@PathVariable String versionId){
		
		ProformaInvoiceVersion piVersion = null;
		
		try {
			piVersion = proformaInvoiceService.setVersionStatus(versionId, model);
		} catch (DataNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(),
					HttpStatus.NOT_FOUND);
		}
				
		return ResponseEntity.ok(piVersion);
	}

}
