package emmet.sales.pi.service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import emmet.sales.entity.pi.ProformaInvoice;
import emmet.sales.entity.pi.ProformaInvoiceExtraCharge;
import emmet.sales.entity.pi.ProformaInvoiceProductItem;
import emmet.sales.entity.pi.ProformaInvoiceVersion;
import emmet.sales.pi.exception.OperationNotPermitException;
import emmet.sales.pi.repository.ProformaInvoiceRepsitory;
import emmet.sales.pi.repository.ProformaInvoiceVersionRepsitory;

@Service
public class ProformaInvoiceService {

	// @Autowired
	// ProformaInvoiceModelFactory proformaInvoiceModelFactory;

	@Autowired
	ProformaInvoiceRepsitory proformaInvoiceRepository;

	@Autowired
	ProformaInvoiceVersionRepsitory proformaInvoiceVersionRepsitory;

	@Transactional
	public ProformaInvoiceVersion createProformaInvoice(ProformaInvoiceVersion thisVersion) {

		ProformaInvoice newProformaInvoice = new ProformaInvoice();
		newProformaInvoice.setConfirmed(false);
		newProformaInvoice = proformaInvoiceRepository.save(newProformaInvoice);

		return persistNewVersion(thisVersion, newProformaInvoice);

	}

	@Transactional
	public ProformaInvoiceVersion updateProformaInvoice(ProformaInvoiceVersion thisVersion)
			throws OperationNotPermitException {
		String proformaInvoiceId = thisVersion.getProformaInvoice().getId();
		ProformaInvoice proformaInvoice = proformaInvoiceRepository.findOne(proformaInvoiceId);

		if (proformaInvoice.isConfirmed()) {
			throw new OperationNotPermitException("This proforma invoice had comfirmed, you can not modify it.");
		}

		return persistNewVersion(thisVersion, proformaInvoice);
	}

	private ProformaInvoiceVersion persistNewVersion(ProformaInvoiceVersion thisVersion,
			ProformaInvoice proformaInvoice) {

		thisVersion.setCreateDateTime(Calendar.getInstance());
		thisVersion.setProformaInvoice(proformaInvoice);
		thisVersion.setVersionSequence(getNextVersionSequence(proformaInvoice.getId()));
		thisVersion.setId(getNewVersionId(proformaInvoice.getId()));

		thisVersion = linkAssociationEntities(thisVersion);

		thisVersion = proformaInvoiceVersionRepsitory.save(thisVersion);

		// Setting up the version
		proformaInvoice.setFinalVersion(thisVersion);
		proformaInvoiceRepository.save(proformaInvoice);

		return thisVersion;
	}

	private String getNewVersionId(String proformaInvoiceId) {

		return proformaInvoiceId + "-" + (getNextVersionSequence(proformaInvoiceId));

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

		return invoiceVersionEntity;

	}

}
