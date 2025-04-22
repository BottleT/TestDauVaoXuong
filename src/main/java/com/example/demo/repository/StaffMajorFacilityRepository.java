package com.example.demo.repository;

import com.example.demo.entity.StaffMajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StaffMajorFacilityRepository extends JpaRepository<StaffMajorFacility, UUID> {

    @Query("SELECT smf FROM StaffMajorFacility smf WHERE smf.staff.id = :staffId")
    List<StaffMajorFacility> findByStaffId(@Param("staffId") UUID staffId);

    @Query("SELECT CASE WHEN COUNT(smf) > 0 THEN true ELSE false END FROM StaffMajorFacility smf " +
            "JOIN smf.majorFacility mf " +
            "JOIN mf.departmentFacility df " +
            "WHERE smf.staff.id = :staffId AND df.facility.id = :facilityId")
    boolean existsByStaffIdAndFacilityId(@Param("staffId") UUID staffId, @Param("facilityId") UUID facilityId);
}