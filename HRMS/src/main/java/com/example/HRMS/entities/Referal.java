package com.example.HRMS.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Referal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long referalId;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "candidate_name", nullable = false)
    private String candidateName;

    @Column(nullable = false)
    private String candidateEmail;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "cv_storage_url", nullable = false, length = 512)
    private String cvURLPath;

    private String shortNote;

    @ManyToOne
    @JoinColumn(name = "referrer_id", nullable = false)
    private Employee referalGivenBy;


}
