package com.example.EShop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "baskets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
   // @JoinColumn(name = "basketId")
    private Set<Product> products = new HashSet<Product>();

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
  //  @JoinColumn(name = "basketId")
    private User user;

}
