package com.example.demo.repository;

import com.example.demo.entity.StaffMajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StaffMajorFacilityRepository extends JpaRepository<StaffMajorFacility, UUID> {
}
