package com.example.demo.controller;

import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffRepository;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api")

public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/home")
    public String listStaff(Model model) {
        List<Staff> staffList = staffService.getAllStaff();
        model.addAttribute("ListStaff", staffService.getAllStaff());
        return "staff/home";
    }

    @GetMapping("/add")
    public String showAddStaffForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "staff/add";
    }

    @PostMapping("/save")
    public String saveStaff(@ModelAttribute Staff staff) {
        staffService.saveStaff(staff);
        return "redirect:/api/home";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Staff staff = staffService.getStaffById(id);
        model.addAttribute("staff", staff);
        return "staff/edit";
    }

    @PostMapping("/update")
    public String updateStaff(@ModelAttribute Staff staff) {
        staffService.updateStaff(staff);
        return "redirect:/api/home";
    }

    @GetMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable UUID id) {
        staffService.toggleStaffStatus(id);
        return "redirect:/api/home";
    }
}
