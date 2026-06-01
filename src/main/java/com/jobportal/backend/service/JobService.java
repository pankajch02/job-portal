package com.jobportal.backend.service;

import com.jobportal.backend.dto.JobRequest;
import com.jobportal.backend.entity.Job;
import com.jobportal.backend.entity.Role;
import com.jobportal.backend.entity.User;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(JobService.class);

    public Job createJob (JobRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository
                .findByEmail(email)
                .orElseThrow();

        if(recruiter.getRole() != Role.RECRUITER){
            logger.warn(
                    "Unauthorized job creation attempt by: {}",
                    recruiter.getEmail()
            );

            throw new RuntimeException(
                    "Only recruiters can create jobs"
            );
        }

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .company(request.getCompany())
                .location(request.getLocation())
                .salary(request.getSalary())
                .recruiter(recruiter)
                .build();

        logger.info(
                "Job created by recruiter: {}",
                recruiter.getEmail()
        );

        return jobRepository.save(job);
    }

    public List<Job> getAllJobs(){

        logger.info("Fetching all jobs");

        return jobRepository.findAll();
    }

    public Job getJobById(Long id){

        return jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found")
                        );
    }

    public void deleteById(Long id){

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository.findByEmail(email)
                        .orElseThrow();

        Job job = jobRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"
                                )
                        );

        if(!job.getRecruiter().getId().equals(recruiter.getId())){

            logger.warn(
                    "Unauthorized delete by: {}",
                    recruiter.getEmail()
            );

            throw new RuntimeException(
                    "You can only delete your own jobs"
            );
        }

        jobRepository.delete(job);

        logger.warn(
                "Job deleted: {} by recruiter: {}",
                id,
                recruiter.getEmail()
        );


    }

    public Job updateJob(
            Long id,
            JobRequest request
    ){
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository
                .findByEmail(email)
                .orElseThrow();

        Job job = jobRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException((
                                "Job not found"
                                ))
                );

        if(!job.getRecruiter().getId().equals(recruiter.getId())){

            logger.warn(
                    "Unauthorized update attempt by: {}",
                    recruiter.getEmail()
            );

            throw new RuntimeException(
                    "You can only update your own jobs"
            );
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());

        logger.info(
                "Job updated: {} by recruiter: {}",
                job.getId(),
                recruiter.getEmail()
        );

        return jobRepository.save(job);
    }




}
