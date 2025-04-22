package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/staff-major")
public class StaffMajorController {

    private final StaffService staffService;
    private final FacilityRepository facilityRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentFacilityRepository departmentFacilityRepository;
    private final MajorRepository majorRepository;
    private final MajorFacilityRepository majorFacilityRepository;
    private final StaffMajorFacilityRepository staffMajorFacilityRepository;

    @Autowired
    public StaffMajorController(
            StaffService staffService,
            FacilityRepository facilityRepository,
            DepartmentRepository departmentRepository,
            DepartmentFacilityRepository departmentFacilityRepository,
            MajorRepository majorRepository,
            MajorFacilityRepository majorFacilityRepository,
            StaffMajorFacilityRepository staffMajorFacilityRepository) {
        this.staffService = staffService;
        this.facilityRepository = facilityRepository;
        this.departmentRepository = departmentRepository;
        this.departmentFacilityRepository = departmentFacilityRepository;
        this.majorRepository = majorRepository;
        this.majorFacilityRepository = majorFacilityRepository;
        this.staffMajorFacilityRepository = staffMajorFacilityRepository;
    }

    @GetMapping("/{id}")
    public String showStaffMajorPage(@PathVariable UUID id, Model model) {
        Staff staff = staffService.getStaffById(id);
        model.addAttribute("staff", staff);

        List<Facility> facilities = facilityRepository.findAll();
        model.addAttribute("facilities", facilities);

        List<StaffMajorFacility> staffMajors = staffMajorFacilityRepository.findByStaffId(id);
        model.addAttribute("staffMajors", staffMajors);

        return "staff/staff-major";
    }

    @GetMapping("/departments")
    @ResponseBody
    public List<DepartmentFacility> getDepartmentsByFacility(@RequestParam UUID facilityId) {
        return departmentFacilityRepository.findByFacilityId(facilityId);
    }

    @GetMapping("/majors")
    @ResponseBody
    public List<MajorFacility> getMajorsByDepartmentFacility(@RequestParam UUID departmentFacilityId) {
        return majorFacilityRepository.findByDepartmentFacilityId(departmentFacilityId);
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addStaffMajor(@RequestBody Map<String, String> payload) {
        UUID staffId = UUID.fromString(payload.get("staffId"));
        UUID majorFacilityId = UUID.fromString(payload.get("majorFacilityId"));

        MajorFacility majorFacility = majorFacilityRepository.findById(majorFacilityId)
                .orElseThrow(() -> new RuntimeException("Major Facility not found"));

        DepartmentFacility departmentFacility = majorFacility.getDepartmentFacility();
        Facility facility = departmentFacility.getFacility();

        boolean alreadyAssigned = staffMajorFacilityRepository.existsByStaffIdAndFacilityId(staffId, facility.getId());

        if (alreadyAssigned) {
            return ResponseEntity.badRequest().body("Nhân viên đã được phân công bộ môn trong cơ sở này");
        }

        StaffMajorFacility staffMajorFacility = new StaffMajorFacility();
        staffMajorFacility.setId(UUID.randomUUID());
        staffMajorFacility.setStaff(staffService.getStaffById(staffId));
        staffMajorFacility.setMajorFacility(majorFacility);
        staffMajorFacility.setStatus((byte) 1);
        staffMajorFacility.setCreatedDate(Instant.now().toEpochMilli());
        staffMajorFacility.setLastModifiedDate(Instant.now().toEpochMilli());

        staffMajorFacilityRepository.save(staffMajorFacility);

        return ResponseEntity.ok().body("Thêm bộ môn chuyên ngành thành công");
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteStaffMajor(@PathVariable UUID id) {
        try {
            staffMajorFacilityRepository.deleteById(id);
            return ResponseEntity.ok().body("Xóa bộ môn chuyên ngành thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa: " + e.getMessage());
        }
    }
}