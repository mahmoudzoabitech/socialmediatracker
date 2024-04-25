package com.tsofnsalesforce.LoginandRegistration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsofnsalesforce.LoginandRegistration.controller.AuthenticationController;
import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
@EntityListeners(AuthenticationController.class)
public class Role {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<AppUser> appUsers;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;


}