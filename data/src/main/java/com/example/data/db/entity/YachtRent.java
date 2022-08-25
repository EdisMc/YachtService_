package com.example.data.db.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "rents")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YachtRent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long yachtId;
    private Long customerId;
    private Long employeeId;
    private Integer daysForRent;
    private Double price;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "yachtId", insertable = false, updatable = false)
    private Yacht yacht;

    @ManyToOne
    @JoinColumn(name = "employeeId", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "customerId", insertable = false, updatable = false)
    private Customer customer;

}
