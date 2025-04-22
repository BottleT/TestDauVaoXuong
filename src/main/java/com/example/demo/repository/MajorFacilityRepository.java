package com.example.demo.repository;

import com.example.demo.entity.MajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MajorFacilityRepository extends JpaRepository<MajorFacility, UUID> {

    @Query("SELECT mf FROM MajorFacility mf WHERE mf.departmentFacility.id = :departmentFacilityId")
    List<MajorFacility> findByDepartmentFacilityId(@Param("departmentFacilityId") UUID departmentFacilityId);
}