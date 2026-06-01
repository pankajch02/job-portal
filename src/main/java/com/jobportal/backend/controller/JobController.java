package com.jobportal.backend.controller;

import com.jobportal.backend.dto.JobRequest;
import com.jobportal.backend.entity.Job;
import com.jobportal.backend.service.JobService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {


    private final JobService jobService;

    @PostMapping
    public Job createJob(
            @RequestBody JobRequest request
            ){
        return jobService.createJob(request);
    }

    @GetMapping
    public List<Job> getAllJobs(){
        return jobService.getAllJobs();
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
            @PathVariable Long id, 
            @RequestBody JobRequest request){
        return jobService.updateJob(id,request);
    }

}
