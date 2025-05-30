package com.example.demo.controller;

import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.contains;


@SpringBootTest
@AutoConfigureMockMvc
//@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {

        UserEntity user = new UserEntity();
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setPassword("admin");
        user.setEmail("admin@admin.com");


            userRepository.save(user);

            String teamName = user.getFirstName() + "'s Team";
            TeamEntity team = new TeamEntity(teamName, "Your private team", Set.of(user), null);
            teamRepository.save(team);
            user.setTeams(new HashSet<TeamEntity>() {{
                this.add(team);
            }});
            userRepository.save(user);
        }

    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testAuthenticateUser() throws Exception {

        mockMvc.perform(get("/users")
                        .with(user("admin@admin.com").roles("ADMIN")))

                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$[0].id").exists())
//                .andExpect(jsonPath("$[0].email").value("admin@admin.com"));
//                .andExpect(jsonPath("$.description").value("test description"));
    }

    @Test
    public void testAuthenticateUser_NotFound() throws Exception {
        userRepository.deleteAll();

        mockMvc.perform(get("/users")
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSearchByEmail() throws Exception {

        String email = "admin@admin.com";

        mockMvc.perform(get("/users/search-by-email/{email}", email)
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("admin@admin.com"));
    }

    @Test
    public void testSearchByEmail_NotFound() throws Exception {

        String email = "admin1@admin.com";

        mockMvc.perform(get("/users/search-by-email/{email}", email)
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void testUpdateUser() throws Exception {
        String jsonRequest = """
                {
                    "id": 1,
                    "firstName": "Taras",
                    "lastName": "Pryimachuk",
                    "avatar": null,
                    "email": "x@d",
                    "role": null,
                    "teamIds": [
                        1
                    ]
                }
                """;

        mockMvc.perform(post("/users/update")
                        .with(user("admin@admin.com").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("x@d"));
//                .andExpect(jsonPath("$.description").value("test description"));
    }

}