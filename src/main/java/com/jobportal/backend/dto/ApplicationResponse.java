package com.jobportal.backend.dto;

import com.jobportal.backend.entity.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationResponse {

    private Long applicationId;

    private Long jobId;

    private String jobTitle;

    private String candidateName;

    private String candidateEmail;

    private ApplicationStatus status;
}
