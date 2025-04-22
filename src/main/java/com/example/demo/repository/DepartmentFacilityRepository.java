package com.example.demo.repository;

import com.example.demo.entity.DepartmentFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DepartmentFacilityRepository extends JpaRepository<DepartmentFacility, UUID> {

    @Query("SELECT df FROM DepartmentFacility df WHERE df.facility.id = :facilityId")
    List<DepartmentFacility> findByFacilityId(@Param("facilityId") UUID facilityId);
}