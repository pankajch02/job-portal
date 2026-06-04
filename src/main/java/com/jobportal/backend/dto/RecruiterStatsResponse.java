package com.jobportal.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecruiterStatsResponse {

    private Long jobsPosted;

    private Long applicationReceived;

    private Long shortlisted;

    private Long hired;
}
