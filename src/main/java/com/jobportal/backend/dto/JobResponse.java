package com.jobportal.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobResponse {

    private Long id;

    private String title;

    private String company;

    private String location;

    private Double salary;

}
