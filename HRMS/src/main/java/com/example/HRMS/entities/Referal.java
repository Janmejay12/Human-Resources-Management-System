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

    @Column(name = "friend_name", nullable = false)
    private String friendName;

    @Column(nullable = false)
    private String email;

    @Column(name = "cv_storage_url", nullable = false, length = 512)
    private String cvURLPath;

    private String shortNote;

    @ManyToOne
    @JoinColumn(name = "referal_given_by_id", nullable = false)
    private Employee referalGivenBy;


}
