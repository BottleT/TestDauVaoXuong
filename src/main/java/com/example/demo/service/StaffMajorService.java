package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class StaffMajorService {

    private final StaffRepository staffRepository;
    private final FacilityRepository facilityRepository;
    private final DepartmentFacilityRepository departmentFacilityRepository;
    private final MajorFacilityRepository majorFacilityRepository;
    private final StaffMajorFacilityRepository staffMajorFacilityRepository;
    private final StaffService staffService;

    @Autowired
    public StaffMajorService(
            StaffRepository staffRepository,
            FacilityRepository facilityRepository,
            DepartmentFacilityRepository departmentFacilityRepository,
            MajorFacilityRepository majorFacilityRepository,
            StaffMajorFacilityRepository staffMajorFacilityRepository,
            StaffService staffService) {
        this.staffRepository = staffRepository;
        this.facilityRepository = facilityRepository;
        this.departmentFacilityRepository = departmentFacilityRepository;
        this.majorFacilityRepository = majorFacilityRepository;
        this.staffMajorFacilityRepository = staffMajorFacilityRepository;
        this.staffService = staffService;
    }

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public List<DepartmentFacility> getDepartmentsByFacility(UUID facilityId) {
        return departmentFacilityRepository.findByFacilityId(facilityId);
    }

    public List<MajorFacility> getMajorsByDepartmentFacility(UUID departmentFacilityId) {
        return majorFacilityRepository.findByDepartmentFacilityId(departmentFacilityId);
    }

    public List<StaffMajorFacility> getStaffMajors(UUID staffId) {
        return staffMajorFacilityRepository.findByStaffId(staffId);
    }

    public boolean isStaffAssignedToFacility(UUID staffId, UUID facilityId) {
        return staffMajorFacilityRepository.existsByStaffIdAndFacilityId(staffId, facilityId);
    }

    @Transactional
    public StaffMajorFacility addStaffMajor(UUID staffId, UUID majorFacilityId) throws Exception {
        Staff staff = staffService.getStaffById(staffId);

        MajorFacility majorFacility = majorFacilityRepository.findById(majorFacilityId)
                .orElseThrow(() -> new Exception("Không tìm thấy chuyên ngành bộ môn với ID: " + majorFacilityId));

        DepartmentFacility departmentFacility = majorFacility.getDepartmentFacility();
        Facility facility = departmentFacility.getFacility();

        if (isStaffAssignedToFacility(staffId, facility.getId())) {
            throw new Exception("Nhân viên đã được phân công bộ môn trong cơ sở này");
        }

        StaffMajorFacility staffMajorFacility = new StaffMajorFacility();
        staffMajorFacility.setId(UUID.randomUUID());
        staffMajorFacility.setStaff(staff);
        staffMajorFacility.setMajorFacility(majorFacility);
        staffMajorFacility.setStatus((byte) 1);
        staffMajorFacility.setCreatedDate(Instant.now().toEpochMilli());
        staffMajorFacility.setLastModifiedDate(Instant.now().toEpochMilli());

        return staffMajorFacilityRepository.save(staffMajorFacility);
    }

    @Transactional
    public void deleteStaffMajor(UUID id) throws Exception {
        if (!staffMajorFacilityRepository.existsById(id)) {
            throw new Exception("Không tìm thấy bản ghi với ID: " + id);
        }
        staffMajorFacilityRepository.deleteById(id);
    }
}