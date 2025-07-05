package com.project.demo.rest.school;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.school.School;
import com.project.demo.logic.entity.school.SchoolRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/schools")
public class SchoolRestController {

    @Autowired
    private SchoolRepository schoolRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<School> schoolPage = schoolRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(schoolPage.getTotalPages());
        meta.setTotalElements(schoolPage.getTotalElements());
        meta.setPageNumber(schoolPage.getNumber() + 1);
        meta.setPageSize(schoolPage.getSize());

        return new GlobalResponseHandler().handleResponse("Schools retrieved successfully",
                schoolPage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/{schoolId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getSchoolById(@PathVariable Long schoolId, HttpServletRequest request) {
        Optional<School> foundSchool = schoolRepository.findById(schoolId);
        if (foundSchool.isPresent()) {
            return new GlobalResponseHandler().handleResponse(
                    "School retrieved successfully",
                    foundSchool.get(),
                    HttpStatus.OK,
                    request);
        } else {
            return new GlobalResponseHandler().handleResponse(
                    "School with ID " + schoolId + " not found",
                    HttpStatus.NOT_FOUND,
                    request);
        }
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> addSchool(@RequestBody School school, HttpServletRequest request) {
        schoolRepository.save(school);
        return new GlobalResponseHandler().handleResponse("School created successfully",
                school, HttpStatus.OK, request);
    }

    @PutMapping("/{schoolId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateSchool(@PathVariable Long schoolId, @RequestBody School school, HttpServletRequest request) {
        Optional<School> foundSchool = schoolRepository.findById(schoolId);
        if (foundSchool.isPresent()) {
            School updatedSchool = foundSchool.get();
            updatedSchool.setName(school.getName());
            updatedSchool.setDomain(school.getDomain());
            schoolRepository.save(updatedSchool);
            return new GlobalResponseHandler().handleResponse("School updated sucessfully",
                    school, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("School id " + schoolId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{schoolId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteSchool(@PathVariable Long schoolId, HttpServletRequest request) {
        Optional<School> foundSchool = schoolRepository.findById(schoolId);
        if (foundSchool.isPresent()) {
            schoolRepository.deleteById(schoolId);
            return new GlobalResponseHandler().handleResponse("School deleted successfully",
                    foundSchool.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("School " + schoolId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

}
