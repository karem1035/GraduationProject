package com.example.Toda;

import com.example.Toda.Entity.Role;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepoTest {
    @Autowired
    private UserRepo userRepo;

    @Test
    void should_save_user_in_database()
    {
       UserEntity user = new UserEntity();
       user.setUsername("amrNada");
        user.setEmail("amr@test.com");
        user.setPassword("123456");
        user.setRole(Role.TOURIST);
       UserEntity userTest =userRepo.save(user);
       assertThat(userTest.getEmail()).isNotNull();

    }

}
