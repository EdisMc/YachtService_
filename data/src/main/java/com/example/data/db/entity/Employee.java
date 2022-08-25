package com.example.data.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "employees")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private Long positionId;

    @OneToMany(mappedBy = "employee")
    private Set<YachtRent> yachtRentEntities;

    @ManyToOne
    @JoinColumn(name = "positionId", insertable = false, updatable = false)
    private Position position;

}
