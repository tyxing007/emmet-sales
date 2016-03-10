package emmet.sales.pi.service;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import emmet.sales.entity.pi.ProformaInvoice;
import emmet.sales.entity.pi.ProformaInvoiceExtraCharge;
import emmet.sales.entity.pi.ProformaInvoiceProductItem;
import emmet.sales.entity.pi.ProformaInvoiceVersion;
import emmet.sales.pi.exception.DataNotFoundException;
import emmet.sales.pi.exception.OperationNotPermitException;
import emmet.sales.pi.repository.ProformaInvoiceRepsitory;
import emmet.sales.pi.repository.ProformaInvoiceVersionRepsitory;

@Service
public class ProformaInvoiceService {

	@Autowired
	ProformaInvoiceRepsitory proformaInvoiceRepsitory;

	@Autowired
	ProformaInvoiceRepsitory proformaInvoiceRepository;

	@Autowired
	ProformaInvoiceVersionRepsitory proformaInvoiceVersionRepsitory;

	@Transactional
	public ProformaInvoice createProformaInvoice(ProformaInvoiceVersion thisVersion) {

		ProformaInvoice newProformaInvoice = new ProformaInvoice();
		newProformaInvoice.setConfirmed(false);
		newProformaInvoice = proformaInvoiceRepository.save(newProformaInvoice);
		persistNewVersion(thisVersion, newProformaInvoice);
		return thisVersion.getProformaInvoice();

	}

	@Transactional
	public ProformaInvoice updateProformaInvoice(ProformaInvoiceVersion thisVersion, String id)
			throws OperationNotPermitException, DataNotFoundException {

		ProformaInvoice proformaInvoice = proformaInvoiceRepsitory.findOne(id);
		if (proformaInvoice == null) {
			throw new DataNotFoundException("Can not find the proforma invoice, ID:" + id);
		}

		thisVersion.setProformaInvoice(proformaInvoice);

		if (proformaInvoice.isConfirmed()) {
			throw new OperationNotPermitException("This proforma invoice had comfirmed, you can not modify it.");
		}

		if (!thisVersion.getInfo().getCustomer().getId()//
				.equals(proformaInvoice.getFinalVersion().getInfo().getCustomer().getId())) {
			throw new OperationNotPermitException("Modify the customer of an invoice is not permited.");
		}

		persistNewVersion(thisVersion, proformaInvoice);
		return thisVersion.getProformaInvoice();
	}

	private ProformaInvoiceVersion persistNewVersion(ProformaInvoiceVersion thisVersion,
			ProformaInvoice proformaInvoice) {

		thisVersion.setCreateDateTime(Calendar.getInstance());
		thisVersion.setProformaInvoice(proformaInvoice);
		thisVersion.setVersionSequence(getNextVersionSequence(proformaInvoice.getId()));
		thisVersion.setId(getNewVersionId(proformaInvoice.getId()));

		thisVersion = linkAssociationEntities(thisVersion);

		thisVersion = proformaInvoiceVersionRepsitory.save(thisVersion);
		saveSnapshoot(thisVersion);

		// Setting up the version
		proformaInvoice.setFinalVersion(thisVersion);
		proformaInvoiceRepository.save(proformaInvoice);

		return thisVersion;
	}

	private String getNewVersionId(String proformaInvoiceId) {

		return proformaInvoiceId + "-" + (getNextVersionSequence(proformaInvoiceId));

	}

	private void saveSnapshoot(ProformaInvoiceVersion piVersion) {
		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, piVersion);
		} catch (IOException e) {
			e.printStackTrace();
		}
		piVersion.setSnapshot(writer.toString());

		proformaInvoiceVersionRepsitory.save(piVersion);

	}

	private Integer getNextVersionSequence(String proformaInvoiceId) {
		if (proformaInvoiceVersionRepsitory.findByProformaInvoiceId(proformaInvoiceId).isEmpty()) {
			return 1;
		}

		return proformaInvoiceVersionRepsitory.findLastVersionSequenceOfProformainvoice(proformaInvoiceId) + 1;
	}

	private ProformaInvoiceVersion linkAssociationEntities(ProformaInvoiceVersion invoiceVersionEntity) {

		// Invoice Info
		if (invoiceVersionEntity.getInfo() != null) {
			invoiceVersionEntity.getInfo().setVersion(invoiceVersionEntity);
			invoiceVersionEntity.getInfo().setCreateDate(new Date(System.currentTimeMillis()));
		}

		// Extra Charge
		List<ProformaInvoiceExtraCharge> extraChagres = invoiceVersionEntity.getExtraCharges();
		if (extraChagres != null && !extraChagres.isEmpty()) {

			for (ProformaInvoiceExtraCharge extraCharge : extraChagres) {
				extraCharge.setVersion(invoiceVersionEntity);
			}
		}

		// ProductItems
		List<ProformaInvoiceProductItem> productItems = invoiceVersionEntity.getProductItems();
		if (productItems != null && !productItems.isEmpty()) {

			for (ProformaInvoiceProductItem productItem : productItems) {
				productItem.setVersion(invoiceVersionEntity);
			}
		}

		// Shipping
		if (invoiceVersionEntity.getShipping() != null) {
			invoiceVersionEntity.getShipping().setVersion(invoiceVersionEntity);
		}

		return invoiceVersionEntity;

	}

	public ProformaInvoiceVersion findProformaInvoiceWithVersion(String proformaInvoiceId, String versionId) throws DataNotFoundException {

		ProformaInvoiceVersion version = proformaInvoiceVersionRepsitory.findByProformaInvoiceIdAndId(proformaInvoiceId, versionId);
		if(version == null){
			throw new DataNotFoundException("The version is not existed.");
		}
		
		return version;

	}

}
