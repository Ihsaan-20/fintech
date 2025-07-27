package com.example.fintech.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cards", uniqueConstraints = {
        @UniqueConstraint(columnNames = "cardNumber")
})
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String cardNumber;

    private String expiryDate;

    private String cvv;

    private String cardHolderName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
