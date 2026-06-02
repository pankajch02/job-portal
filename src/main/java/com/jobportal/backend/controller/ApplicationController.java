package com.jobportal.backend.controller;


import com.jobportal.backend.dto.UpdateApplicationStatusRequest;
import com.jobportal.backend.entity.Application;
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
    public Application applyForJob(
            @PathVariable Long jobId
    ) {
        return applicationService.applyForJob(jobId);
    }

    @GetMapping("/my")
    public List<Application> getMyApplications(){

        return applicationService.getMyApplications();
    }

    @GetMapping("/job/{jobId}")
    public List<Application> getApplicantsForJob(
            @PathVariable Long jobId
    ) {

        return applicationService.getApplicantsForJob(jobId);
    }

    @PutMapping("/{applicationId}/status")
    public Application updateStatus(
            @PathVariable Long applicationId,
            @RequestBody UpdateApplicationStatusRequest request
            ) {
        return applicationService.updateStatus(applicationId, request.getStatus());
    }
}
