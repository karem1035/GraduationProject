package com.example.Toda.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id ;
    @Column(name = "userName", nullable = false, length = 50)
    String Username;
    @Column(name = "email", nullable = false,unique = true, length = 50)
    String email;
    @Column(name = "password", nullable = false,unique = true, length = 255)
    String Password;
    @Enumerated(EnumType.STRING)
    Role role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private TourGuideEntity tourGuide;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private TouristProfileEntity tourristProfile;


}
