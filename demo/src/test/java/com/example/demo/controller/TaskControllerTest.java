package com.example.demo.controller;

import com.example.demo.entities.TaskEntity;
import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.TaskService;
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

import java.time.LocalDateTime;
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
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TaskService taskService;

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


            TaskEntity task = new TaskEntity();
//        task.setId(1L);
            task.setTitle("Example Task Title");
            task.setDescription("Example description for the task.");
            task.setFromDate(LocalDateTime.now());
            task.setToDate(LocalDateTime.now().plusDays(7));
            task.setStatus(null); // zakładamy, że masz enum TaskStatus z np. TODO, IN_PROGRESS, DONE
            task.setPriority(null); // zakładamy, że masz enum TaskPriority z np. LOW, MEDIUM, HIGH

            // Subtasks (pusta lista na początek, możesz dodać przykładowe SubtaskEntity jeśli chcesz)
            task.setSubtasks(new HashSet<>());

            // Relacje
            task.setCreator(user);
            task.setAssignedUser(user);
            task.setTeam(team);

            taskRepository.save(task);
        }

    }

    @Test
//    @AutoConfigureMockMvc(addFilters = false)
    public void testGetTaskById() throws Exception {
        mockMvc.perform(get("/tasks/{id}", 2L)
                        .with(user("admin@admin.com").roles("ADMIN")))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testGetTaskById_NotFound() throws Exception {
        mockMvc.perform(get("/tasks/{id}", 999L)
                        .with(user("admin@admin.com").roles("ADMIN")))

                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testAddTask() throws Exception {
        String jsonRequest = """
                {
                    "title" : "Zadanie 1",
                    "description": "Opis",
                    "toDate": "2025-08-25T12:00:00",
                    "status": "IN_PROGRESS",
                    "priority": "PRIORITY_1",
                    "subtasks": [
                        {
                            "content": "Zadanko a",
                            "status": "false"
                        },
                        {
                            "content": "Zadanko b",
                            "status": "false"
                        },
                        {
                            "content": "Zadanko c",
                            "status": "true"
                        }
                    ],
                    "assignedUserId": 1,
                    "teamId": 1
                }
                """;

        mockMvc.perform(post("/tasks/add")
                        .with(user("admin@admin.com").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testRemoveTask() throws Exception {
        mockMvc.perform(delete("/tasks/remove/{id}", 1L)
                        .with(user("admin@admin.com").roles("ADMIN")))

                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testRemoveTask_NotFound() throws Exception {
        mockMvc.perform(delete("/tasks/remove/{id}", 999L)
                        .with(user("admin@admin.com").roles("ADMIN")))

                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testUpdateTask() throws Exception {
        String jsonRequest = """
                {
                    "id": 1,
                    "title" : "Zadanie 111",
                    "description": "Opis",
                    "toDate": "2025-08-25T12:00:00",
                    "status": "IN_PROGRESS",
                    "priority": "PRIORITY_1",
                    "subtasks": [
                        {
                            "content": "Zadanko a",
                            "status": "false"
                        },
                        {
                            "content": "Zadanko b",
                            "status": "false"
                        },
                        {
                            "content": "Zadanko c",
                            "status": "true"
                        }
                    ],
                    "assignedUserId": 1,
                    "teamId": 1
                }
                """;

        mockMvc.perform(post("/tasks/update")
                        .with(user("admin@admin.com").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(("Zadanie 111")));

    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testUpdateTask_NotFound() throws Exception {
        String jsonRequest = """
                {
                    "id": 111,
                    "title" : "Zadanie 111",
                    "description": "Opis",
                    "toDate": "2025-08-25T12:00:00",
                    "status": "IN_PROGRESS",
                    "priority": "PRIORITY_1",
                    "subtasks": [
                        {
                            "content": "Zadanko a",
                            "status": "false"
                        },
                        {
                            "content": "Zadanko b",
                            "status": "false"
                        },
                        {
                            "content": "Zadanko c",
                            "status": "true"
                        }
                    ],
                    "assignedUserId": 1,
                    "teamId": 1
                }
                """;

        mockMvc.perform(post("/tasks/update")
                        .with(user("admin@admin.com").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

}