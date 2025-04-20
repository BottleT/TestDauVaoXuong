package com.example.demo.repository;

import com.example.demo.entity.MajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MajorFacilityRepository extends JpaRepository<MajorFacility, UUID> {
}
