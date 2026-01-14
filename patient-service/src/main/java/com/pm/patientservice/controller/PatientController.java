package com.pm.patientservice.controller;


import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name="Patient")
public class PatientController {

    public final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(description = "Get patient entity")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {

        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);

    }

    @PostMapping()
    @Operation(description = "Create new patient entity")
    public ResponseEntity<PatientResponseDTO> createPatient( @Validated({Default.class , CreatePatientValidationGroup.class})
                          @RequestBody PatientRequestDTO patientRequestDTO) {

        PatientResponseDTO responseDTO = patientService.createPatient(patientRequestDTO);

        return ResponseEntity.ok().body(responseDTO);
    }


    @PutMapping("/{id}")
    @Operation(description = "Update patient entity")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
                                  @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {

        PatientResponseDTO updatedResponseDTO = patientService.updatePatient(id, patientRequestDTO);

        return ResponseEntity.ok().body(updatedResponseDTO);
    }
   @DeleteMapping("/{id}")
   @Operation(description = "Delete patient entity")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id){

        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
   }
}
