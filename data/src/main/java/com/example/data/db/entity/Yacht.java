package com.example.data.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "yachts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Yacht {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long yachtId;

    private String number;
    private double price;
    private Boolean status;

    @OneToMany(mappedBy = "yacht")
    private Set<YachtRent> yachtRents;

}
