package com.hbtheme.infigestback.service;

import com.hbtheme.infigestback.dto.PatientRequest;
import com.hbtheme.infigestback.model.Patient;

import java.util.List;

public interface PatientService {

	List<Patient> findPatientById(Long patientId);

	void savePatient(PatientRequest patientRequest, boolean isIdRequired);

	List<Patient> findAllPatients();

	void deletePatientById(Long id);

}
