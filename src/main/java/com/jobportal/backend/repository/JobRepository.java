package com.jobportal.backend.repository;

import com.jobportal.backend.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Override
    Page<Job> findAll(Pageable pageable);

    Page<Job> findByTitleContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );

    Page<Job> findByLocationContainingIgnoreCase(
            String location,
            Pageable pageable
    );

    Page<Job> findByCompanyContainingIgnoreCase(
            String company,
            Pageable pageable
    );
}
