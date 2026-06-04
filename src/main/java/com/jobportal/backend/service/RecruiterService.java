package com.jobportal.backend.service;

import com.jobportal.backend.dto.JobResponse;
import com.jobportal.backend.dto.RecruiterStatsResponse;
import com.jobportal.backend.entity.ApplicationStatus;
import com.jobportal.backend.entity.Role;
import com.jobportal.backend.entity.User;
import com.jobportal.backend.exception.ResourceNotFoundException;
import com.jobportal.backend.exception.UnauthorizedException;
import com.jobportal.backend.repository.ApplicationRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruiterService {

    private final UserRepository userRepository;

    private final JobRepository jobRepository;

    private final ApplicationRepository applicationRepository;

    private User getCurrentRecruiter(){

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() ->new ResourceNotFoundException(
                        "User not found"
                ));

        if(recruiter.getRole() != Role.RECRUITER){

            throw new UnauthorizedException(
                    "Only recruiters allowed"
            );
        }

        return recruiter;
    }

    public List<JobResponse> getMyJobs(){

        User recruiter = getCurrentRecruiter();

        return jobRepository.findByRecruiterId(
                recruiter.getId()
        )
                .stream()
                .map(job -> JobResponse
                        .builder()
                        .id(job.getId())
                        .title(job.getTitle())
                        .company(job.getCompany())
                        .location(job.getLocation())
                        .salary(job.getSalary())
                        .build())
                .toList();
    }

    public RecruiterStatsResponse getDashboardStats(){

        User recruiter = getCurrentRecruiter();

        Long recruiterId = recruiter.getId();

        long jobsPosted = jobRepository
                .findByRecruiterId(recruiterId).size();

        long applicationsReceived = applicationRepository
                .countByJobRecruiterId(
                        recruiterId
                );

        long shortlisted = applicationRepository
                .countByJobRecruiterIdAndStatus(
                        recruiterId,
                        ApplicationStatus.SHORTLISTED
                );

        long hired = applicationRepository.
                countByJobRecruiterIdAndStatus(
                        recruiterId,
                        ApplicationStatus.HIRED
                );

        return RecruiterStatsResponse
                .builder()
                .jobsPosted(jobsPosted)
                .applicationReceived(applicationsReceived)
                .shortlisted(shortlisted)
                .hired(hired)
                .build();
    }
}
