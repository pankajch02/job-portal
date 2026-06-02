package com.jobportal.backend.controller;


import com.jobportal.backend.dto.ApplicationResponse;
import com.jobportal.backend.dto.UpdateApplicationStatusRequest;

import com.jobportal.backend.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/jobs/{jobId}")
    public ApplicationResponse applyForJob(
            @PathVariable Long jobId
    ) {
        return applicationService.applyForJob(jobId);
    }

    @GetMapping("/my")
    public List<ApplicationResponse> getMyApplications(){

        return applicationService.getMyApplications();
    }

    @GetMapping("/job/{jobId}")
    public List<ApplicationResponse> getApplicantsForJob(
            @PathVariable Long jobId
    ) {

        return applicationService.getApplicantsForJob(jobId);
    }

    @PutMapping("/{applicationId}/status")
    public ApplicationResponse updateStatus(
            @PathVariable Long applicationId,
            @RequestBody UpdateApplicationStatusRequest request
            ) {
        return applicationService.updateStatus(applicationId, request.getStatus());
    }
}
