package com.jobportal.backend.service;

import com.jobportal.backend.dto.JobRequest;
import com.jobportal.backend.dto.JobResponse;
import com.jobportal.backend.entity.Job;
import com.jobportal.backend.entity.Role;
import com.jobportal.backend.entity.User;
import com.jobportal.backend.exception.ResourceNotFoundException;
import com.jobportal.backend.exception.UnauthorizedException;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

            throw new UnauthorizedException(
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

    public Page<JobResponse> getAllJobs(
            int page,
            int size
    ){

        logger.info("Fetching all jobs");

        Page<Job> jobs = jobRepository.findAll(
                PageRequest.of(
                        page,
                        size
                )
        );

        return jobs.map(this::mapToResponse);

    }

    public Job getJobById(Long id){

        return jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found")
                        );
    }

    public void deleteById(Long id){

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("User not found")
                        );

        Job job = jobRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Job not found"
                                )
                        );

        if(!job.getRecruiter().getId().equals(recruiter.getId())){

            logger.warn(
                    "Unauthorized delete by: {}",
                    recruiter.getEmail()
            );

            throw new UnauthorizedException(
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
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Job job = jobRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException((
                                "Job not found"
                                ))
                );

        if(!job.getRecruiter().getId().equals(recruiter.getId())){

            logger.warn(
                    "Unauthorized update attempt by: {}",
                    recruiter.getEmail()
            );

            throw new UnauthorizedException(
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

    public Page<JobResponse> searchByTitle(
            String keyword,
            int page,
            int size
    ){

        return jobRepository.findByTitleContainingIgnoreCase(
                keyword,
                PageRequest.of(
                        page,
                        size
                )
        )
                .map(this::mapToResponse);
    }

    public Page<JobResponse> searchByLocation(
            String location,
            int page,
            int size
    ){

        return jobRepository.findByLocationContainingIgnoreCase(
                location,
                PageRequest.of(
                        page,
                        size
                )
        )
                .map(this::mapToResponse);
    }

    public Page<JobResponse> searchByCompany(
            String company,
            int page,
            int size
    ){
        return jobRepository.findByCompanyContainingIgnoreCase(
                company,
                PageRequest.of(
                        page,
                        size
                )
        )
                .map(this::mapToResponse);
    }

private JobResponse mapToResponse(Job job){

        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .company(job.getCompany())
                .location(job.getLocation())
                .salary(job.getSalary())
                .build();
}


}
