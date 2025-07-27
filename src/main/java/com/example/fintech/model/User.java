package com.example.fintech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "mobileNumber"),
        @UniqueConstraint(columnNames = "username")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 40)
    private String firstName; // ✅ Optional

    @Size(max = 40)
    private String middleName; // ✅ Optional

    @Size(max = 40)
    private String lastName; // ✅ Optional

    @Size(max = 100)
    private String password; // ✅ Optional at start

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "Mobile number must be 11 digits")
    @Column(unique = true)
    private String mobileNumber; // ✅ Required

    @Size(max = 100)
    @Column(unique = true)
    private String username; // ✅ Optional at start — generate later

    private BigDecimal balance = BigDecimal.ZERO;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Card card;

    public User(String firstName, String middleName, String lastName, String password, String mobileNumber, String username) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.username = username;
    }

    public User(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
