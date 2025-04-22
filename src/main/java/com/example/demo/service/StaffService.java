package com.example.demo.service;

import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffMajorFacilityRepository;
import com.example.demo.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class StaffService {

    private StaffRepository staffRepository;
    private StaffMajorFacilityRepository staffMajorFacilityRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository, StaffMajorFacilityRepository staffMajorFacilityRepository) {
        this.staffRepository = staffRepository;
        this.staffMajorFacilityRepository = staffMajorFacilityRepository;
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Staff getStaffById(UUID id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new ResourceAccessException("Nhân viên không tìm thấy với id: " + id));
    }

    public Staff saveStaff(Staff staff) {
        if (staff.getId() == null) {
            staff.setId(UUID.randomUUID());
            staff.setCreatedDate(Instant.now().toEpochMilli());
        }
        staff.setLastModifiedDate(Instant.now().toEpochMilli());
        return staffRepository.save(staff);
    }

    public Staff updateStaff(Staff staff) {
        Staff existingStaff = getStaffById(staff.getId());
        existingStaff.setName(staff.getName());
        existingStaff.setStaffCode(staff.getStaffCode());
        existingStaff.setAccountFe(staff.getAccountFe());
        existingStaff.setAccountFpt(staff.getAccountFpt());
        existingStaff.setStatus(staff.getStatus());
        existingStaff.setLastModifiedDate(Instant.now().toEpochMilli());

        return staffRepository.save(existingStaff);
    }

    public Staff toggleStaffStatus(UUID id) {
        Staff staff = getStaffById(id);
        if (staff.getStatus() == 1) {
            staff.setStatus((byte) 0);
        } else {
            staff.setStatus((byte) 1);
        }
        staff.setLastModifiedDate(Instant.now().toEpochMilli());
        return staffRepository.save(staff);
    }

    public void deleteStaff(UUID id) {
        Staff staff = getStaffById(id);
        staffRepository.delete(staff);
    }

}