package com.project.trivia;

import com.project.trivia.User.User;
import com.project.trivia.User.UserController;
import com.project.trivia.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AdamSystemTest {

    @Mock
    private UserRepository userRepo;


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUserByIdTest() throws Exception {
        List<User> userList = new ArrayList<>();
        when(userRepo.findAll()).thenReturn(userList);
        mockMvc.perform(get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}

