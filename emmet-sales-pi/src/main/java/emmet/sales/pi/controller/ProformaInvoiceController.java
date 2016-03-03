package emmet.sales.pi.controller;

import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import emmet.sales.entity.pi.ProformaInvoice;
import emmet.sales.entity.pi.ProformaInvoiceVersion;
import emmet.sales.pi.exception.OperationNotPermitException;
import emmet.sales.pi.repository.ProformaInvoiceRepsitory;
import emmet.sales.pi.service.ProformaInvoiceService;

@RepositoryRestController()
public class ProformaInvoiceController {

	@Autowired
	ProformaInvoiceService proformaInvoiceService;

	@Autowired
	ProformaInvoiceRepsitory proformaInvoiceRepsitory;

	// @Autowired
	// ProformaInvoiceExtraChargeRepsitory proformaInvoiceExtraChargeRepsitory;

	@RequestMapping(value = "/proformaInvoices", method = RequestMethod.GET)
	public ResponseEntity<?> getProformaInvoices() {

		return ResponseEntity.ok(proformaInvoiceRepsitory.findAll());

	}

	@RequestMapping(value = "/proformaInvoices/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getProformaInvoice(@PathVariable String id) {

		return ResponseEntity.ok(proformaInvoiceRepsitory.findOne(id));

	}

	@RequestMapping(value = "/proformaInvoices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveProformaInvoice(@RequestBody ProformaInvoiceVersion invoice) {

		return new ResponseEntity<ProformaInvoice>(proformaInvoiceService.createProformaInvoice(invoice),
				HttpStatus.CREATED);

	}

	@RequestMapping(value = "/proformaInvoices/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
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
}
