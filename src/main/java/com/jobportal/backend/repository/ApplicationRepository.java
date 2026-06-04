package com.jobportal.backend.repository;

import com.jobportal.backend.entity.Application;
import com.jobportal.backend.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

    List<Application> findByCandidateId(Long candidateId);

    List<Application> findByJobId(Long jobId);

    boolean existsByCandidateIdAndJobId(
            Long candidateId,
            Long jobId
    );

    long countByJobRecruiterId(Long recruiterId);

    long countByJobRecruiterIdAndStatus(
            Long recruiterId,
            ApplicationStatus status
    );
}
