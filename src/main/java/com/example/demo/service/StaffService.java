package com.example.demo.service;

import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffMajorFacilityRepository;
import com.example.demo.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

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
                .orElseThrow(() -> new ResourceAccessException("Staff not found with id: " + id));
    }

}
