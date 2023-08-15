package com.hbtheme.infigestback.service.impl;

import com.hbtheme.infigestback.dto.InsuranceRequest;
import com.hbtheme.infigestback.mapper.InsuranceMapper;
import com.hbtheme.infigestback.model.Insurance;
import com.hbtheme.infigestback.repository.InsuranceDao;
import com.hbtheme.infigestback.service.InsuranceService;
import com.hbtheme.infigestback.service.validator.InsuranceValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsuranceServiceImpl implements InsuranceService {

	private final InsuranceDao insuranceDao;

	private final InsuranceValidator insuranceValidator;

	private final InsuranceMapper insuranceMapper;

	@Override
	public void saveInsurance(InsuranceRequest insuranceRequest, boolean isIdRequired) {
		if (insuranceRequest == null) {
			throw new IllegalArgumentException();
		}

		List<String> errors = insuranceValidator.validate(insuranceRequest, isIdRequired);
		if (!errors.isEmpty()) {
			String errorsList = String.join("; ", errors);
			throw new IllegalStateException(errorsList);
		}

		if (isIdRequired) {
			findInsuranceById(insuranceRequest.getId());
		}

		Insurance insurance = insuranceMapper.toModel(insuranceRequest);
		insuranceDao.save(insurance);
	}

	@Override
	public List<Insurance> findAllInsurances() {
		return insuranceDao.findAll();
	}

	@Override
	public void deleteInsuranceById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		findInsuranceById(id);
		insuranceDao.deleteById(id);
	}

	@Override
	public List<Insurance> findInsuranceById(Long insuranceId) {
		if (insuranceId == null) {
			throw new IllegalArgumentException("You need to register a mutual insurance company");
		}
		List<Insurance> insurances = new ArrayList<>();
		insurances.add(insuranceDao.findById(insuranceId).orElseThrow(() ->
				new EntityNotFoundException("Mutual insurance companies not found")));
		return insurances;
	}

}
