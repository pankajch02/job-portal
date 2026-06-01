package com.jobportal.backend.dto;

import com.jobportal.backend.entity.ApplicationStatus;
import lombok.Data;

@Data
public class UpdateApplicationStatusRequest {

    private ApplicationStatus status;

}
