package com.hbtheme.infigestback.service;

import com.hbtheme.infigestback.dto.InsuranceRequest;
import com.hbtheme.infigestback.model.Insurance;

import java.util.List;

public interface InsuranceService {

	void saveInsurance(InsuranceRequest insuranceRequest, boolean isIdRequired);

	List<Insurance> findAllInsurances();

	List<Insurance> findInsuranceById(Long id);

	void deleteInsuranceById(Long id);

}
