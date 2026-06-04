package com.jobportal.backend.controller;

import com.jobportal.backend.dto.JobResponse;
import com.jobportal.backend.dto.RecruiterStatsResponse;
import com.jobportal.backend.service.RecruiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterService recruiterService;

    @GetMapping("/jobs")
    public List<JobResponse> getMyJobs(){
        return recruiterService.getMyJobs();
    }

    @GetMapping("/stats")
    public RecruiterStatsResponse getStats(){
        return recruiterService.getDashboardStats();
    }
}
