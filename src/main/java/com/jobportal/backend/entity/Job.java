package com.jobportal.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Table (name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Job {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String company;

    private String location;

    private Double salary;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;
}
