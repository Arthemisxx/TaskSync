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
@Transactional
class TeamControllerTest {

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

        UserEntity user = new UserEntity();
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setPassword("admin");
        user.setEmail("admin@admin.com");


        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
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
    public void testCreateTeam() throws Exception {
        String jsonRequest = """
                {
                    "teamName": "test team",
                    "description": "test description",
                    "userIds": []
                }
                """;

        mockMvc.perform(post("/teams/create-team")
                        .with(user("admin@admin.com").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.teamName").value("test team"))
//                .andExpect(jsonPath("$.description").value("test description"));
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testGetAllUserTeams_NotFound() throws Exception {

        UserEntity user = userRepository.findByEmail("admin@admin.com").get();
        user.setTeams(new HashSet<TeamEntity>());

        mockMvc.perform(get("/teams")
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testGetAllUserTeams_oneTeam() throws Exception {
        mockMvc.perform(get("/teams")
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].teamName").value("admin's Team"))
                .andExpect(jsonPath("$[0].description").value("Your private team"))
                .andExpect(jsonPath("$[0].userIds").exists());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testGetAllUserTeams_twoTeams() throws Exception {

        Optional<UserEntity> userOpt = userRepository.findByEmail("admin@admin.com");
        UserEntity user = userOpt.orElseThrow(() -> new RuntimeException("User not found"));
        String teamName = user.getFirstName() + "'s Team";
        TeamEntity team = new TeamEntity(teamName, "Your private team", Set.of(user), null);
        teamRepository.save(team);
        Set<TeamEntity> existingTeams = user.getTeams();
        if (existingTeams == null) {
            existingTeams = new HashSet<>();
        }
        existingTeams.add(team);
        user.setTeams(existingTeams);
        userRepository.save(user);
        //        userRepository.save(user);

        mockMvc.perform(get("/teams")
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testGetTeamById() throws Exception {
        UserEntity user = userRepository.findByEmail("admin@admin.com").orElseThrow(() -> new RuntimeException("User not found"));

        TeamEntity team = new TeamEntity("teamName", "Your private team", Set.of(user), null);
        teamRepository.save(team);
        Long id = team.getId();

        mockMvc.perform(get("/teams/team-id/{id}", id)
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.teamName").value("teamName"))
                .andExpect(jsonPath("$.description").value("Your private team"));
//                .andExpect(jsonPath("$.userIds", contains(1)));
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testGetTeamById_NotFound() throws Exception {
        mockMvc.perform(get("/teams/team-id/{id}", 9999)
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testAddUser() throws Exception {

        UserEntity user1 = userRepository.findByEmail("admin@admin.com").orElseThrow(() -> new RuntimeException("User not found"));

        UserEntity user = new UserEntity();
        user.setFirstName("admin1");
        user.setLastName("admin1");
        user.setPassword("admin1");
        user.setEmail("admin1@admin.com");
        user.setTeams(new HashSet<>());
        userRepository.save(user);
        Long userId = user.getId();



        TeamEntity team = new TeamEntity("teamName", "Your private team", new HashSet<>(List.of(user1)), null);
//        teamRepository.save(team);
        String teamName = user.getFirstName() + "'s Team";
        teamRepository.save(team);
        Long teamId = team.getId();

        user1.setTeams(new HashSet<TeamEntity>() {{
            this.add(team);
        }});

        mockMvc.perform(post("/teams/add-user/{teamId}/{userId}", teamId, userId)
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testAddUser_NotFoundForUser() throws Exception {

        UserEntity user = userRepository.findByEmail("admin@admin.com").orElseThrow(() -> new RuntimeException("User not found"));



        TeamEntity team = new TeamEntity("teamName", "Your private team", new HashSet<>(List.of(user)), null);
        String teamName = user.getFirstName() + "'s Team";
        teamRepository.save(team);
        Long teamId = team.getId();

        mockMvc.perform(post("/teams/add-user/{teamId}/{userId}", teamId, 999)
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testAddUser_NotFoundForTeam() throws Exception {

        UserEntity user = userRepository.findByEmail("admin@admin.com").orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = user.getId();

        mockMvc.perform(post("/teams/add-user/{teamId}/{userId}", 999, userId)
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testRemoveUser() throws Exception {

        UserEntity user = userRepository.findByEmail("admin@admin.com").orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();

        String teamName = user.getFirstName() + "'s Team";
        TeamEntity team = new TeamEntity(teamName, "Your private team", new HashSet<>(List.of(user)), null);
        teamRepository.save(team);
        user.setTeams(new HashSet<TeamEntity>() {{
            this.add(team);
        }});
        Long teamId = team.getId();
        userRepository.save(user);

        mockMvc.perform(delete("/teams/remove-user/{teamId}/{userId}", teamId, userId)
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testRemoveUser_NotFoundForUser() throws Exception {

        UserEntity user = userRepository.findByEmail("admin@admin.com").orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();

        String teamName = user.getFirstName() + "'s Team";
        TeamEntity team = new TeamEntity(teamName, "Your private team", new HashSet<>(List.of(user)), null);
        teamRepository.save(team);
        user.setTeams(new HashSet<TeamEntity>() {{
            this.add(team);
        }});
        Long teamId = team.getId();
        userRepository.save(user);

        mockMvc.perform(delete("/teams/remove-user/{teamId}/{userId}", teamId, 999)
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testRemoveUser_NotFoundForTeam() throws Exception {

        UserEntity user = userRepository.findByEmail("admin@admin.com").orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();

        String teamName = user.getFirstName() + "'s Team";
        TeamEntity team = new TeamEntity(teamName, "Your private team", new HashSet<>(List.of(user)), null);
        teamRepository.save(team);
        user.setTeams(new HashSet<TeamEntity>() {{
            this.add(team);
        }});
        Long teamId = team.getId();
        userRepository.save(user);

        mockMvc.perform(delete("/teams/remove-user/{teamId}/{userId}", 999, userId)
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}