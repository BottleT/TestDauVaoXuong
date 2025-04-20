package com.example.demo.service;

import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffMajorFacilityRepository;
import com.example.demo.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.UUID;

// Đánh dấu lớp này là một bean kiểu Service để Spring quản lý và tự động inject
@Service

public class StaffService {

    // Khai báo các repository cần dùng (tương tác với database)
    private StaffRepository staffRepository;
    private StaffMajorFacilityRepository staffMajorFacilityRepository;

    // Constructor để Spring tự inject 2 repository vào service thông qua @Autowired
    @Autowired
    public StaffService(StaffRepository staffRepository, StaffMajorFacilityRepository staffMajorFacilityRepository) {
        this.staffRepository = staffRepository;
        this.staffMajorFacilityRepository = staffMajorFacilityRepository;
    }

    // Lấy danh sách tất cả nhân viên từ cơ sở dữ liệu
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    // Tìm nhân viên theo ID. Nếu không tìm thấy sẽ ném ra lỗi ResourceAccessException
    public Staff getStaffById(UUID id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new ResourceAccessException("Staff not found with id: " + id));
    }

}
