package com.example.Toda.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

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
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "deletion_date")
    private LocalDateTime deletionDate;
    @Enumerated(EnumType.STRING)
    Role role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TourGuideEntity tourGuide;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TouristProfileEntity tourristProfile;


}
