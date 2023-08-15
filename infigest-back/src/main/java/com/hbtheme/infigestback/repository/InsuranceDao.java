package com.hbtheme.infigestback.repository;

import com.hbtheme.infigestback.model.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceDao extends JpaRepository<Insurance, Long> {
    List<Insurance> findAllByIdIn(List<Long> insuranceId);
}
