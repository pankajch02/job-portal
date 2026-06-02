package com.jobportal.backend.service;


import com.jobportal.backend.entity.*;
import com.jobportal.backend.exception.BadRequestException;
import com.jobportal.backend.exception.ResourceNotFoundException;
import com.jobportal.backend.exception.UnauthorizedException;
import com.jobportal.backend.repository.ApplicationRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {


    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(
                ApplicationService.class
            );

    public Application applyForJob(Long jobId){

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        if(candidate.getRole() != Role.CANDIDATE){

            throw new UnauthorizedException(
                    "Only candidates can apply"
            );
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found"
                        )
                );

        boolean alreadyApplied = applicationRepository
                .existsByCandidateIdAndJobId(candidate.getId(), jobId);

        if(alreadyApplied) {

            throw new BadRequestException(
                    "Already applied for this job"
            );
        }

        Application application = Application.builder()
                .candidate(candidate)
                .job(job)
                .status(
                        ApplicationStatus.APPLIED
                )
                .build();

        logger.info(
                "Candidate {}, applied for job {}",
                candidate.getEmail(),
                job.getId()
        );

        return applicationRepository.save(application);

    }

    public List<Application> getMyApplications(){

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return applicationRepository.findByCandidateId(
                candidate.getId()
        );
    }

    public List<Application> getApplicantsForJob(Long jobId){

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User not found"
                        )
                );

        Job job = jobRepository.findById(jobId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Job not found"
                        )
                );

        if(!job.getRecruiter().getId()
                .equals(recruiter.getId())){

            throw new UnauthorizedException(
                    "You do not own this job"
            );
        }

        return applicationRepository.findByJobId(jobId);
    }

    public Application updateStatus(
            Long applicationId,
            ApplicationStatus status
    ){

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User not found"
                        )
                );

        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found"
                ));
        if(!application.getJob().getRecruiter().getId()
                .equals(recruiter.getId())){

            throw new UnauthorizedException(
                    "You do not own this job"
            );
        }

        application.setStatus(status);

        logger.info(
                "Application {} status changed to {}",
                applicationId,
                status
        );

        return applicationRepository.save(application);


    }


}
