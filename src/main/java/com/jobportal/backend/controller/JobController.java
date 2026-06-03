package com.jobportal.backend.controller;

import com.jobportal.backend.dto.JobRequest;
import com.jobportal.backend.dto.JobResponse;
import com.jobportal.backend.entity.Job;
import com.jobportal.backend.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {


    private final JobService jobService;

    @PostMapping
    public Job createJob(
            @Valid
            @RequestBody JobRequest request
            ){
        return jobService.createJob(request);
    }

    @GetMapping
    public Page<JobResponse> getAllJobs(
            @RequestParam(
                    defaultValue = "0"
            ) int page,
            @RequestParam(
                    defaultValue = "10"
            ) int size
            ){
        return jobService.getAllJobs(
                page, size
        );
    }

    @GetMapping("/search/title")
    public Page<JobResponse> searchByTitle(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "10")
            int size
    ){

        return jobService.searchByTitle(keyword,page,size);
    }

    @GetMapping("/search/location")
    public Page<JobResponse> searchByLocation(
            @RequestParam String location,
            @RequestParam(
                    defaultValue = "0"
            ) int page,
            @RequestParam(defaultValue = "10")
            int size
    ){
        return jobService
                .searchByLocation(
                        location,
                        page,
                        size
                );
    }

    @GetMapping("/search/company")
    public Page<JobResponse>
    searchByCompany(

            @RequestParam String company,

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size
    ) {

        return jobService
                .searchByCompany(
                        company,
                        page,
                        size
                );
    }


    @GetMapping("/{id}")
    public Job getJobById(@PathVariable Long id){
        return jobService.getJobById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteJob(
            @PathVariable Long id
    ){
        jobService.deleteById(id);
    }

    @PostMapping("/{id}")
    public Job updateJob(
            @Valid
            @PathVariable Long id, 
            @RequestBody JobRequest request){
        return jobService.updateJob(id,request);
    }

}
