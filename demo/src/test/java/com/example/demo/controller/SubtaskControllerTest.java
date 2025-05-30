package com.example.demo.controller;

import com.example.demo.entities.SubtaskEntity;
import com.example.demo.entities.TaskEntity;
import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.SubtaskRepository;
import com.example.demo.repositories.TaskRepository;
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
//@Transactional
class SubtaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SubtaskRepository subtaskRepository;

    @BeforeEach
    void setUp() {
        if (!userRepository.findByEmail("admin@admin.com").isPresent()) {

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
            taskRepository.save(task);


            // Subtasks (pusta lista na początek, możesz dodać przykładowe SubtaskEntity jeśli chcesz)
            SubtaskEntity sub1 = new SubtaskEntity();
//            sub1.setId(1L);
            sub1.setContent("Subtask 1");
            sub1.setMainTask(task); // ważne: ustaw powiązanie do głównego taska
            sub1.setStatus(false);

            SubtaskEntity sub2 = new SubtaskEntity();
//            sub2.setId(2L);
            sub2.setContent("Subtask 2");
            sub2.setMainTask(task);
            System.out.println(task.getId() + "-------------------------------------------------------------------------------------------");
            sub2.setStatus(false);




            Set<SubtaskEntity> subtaskSet = new HashSet<>();
            subtaskSet.add(sub1);
            subtaskSet.add(sub2);

            task.setSubtasks(subtaskSet);

            // Relacje
            task.setCreator(user);
            task.setAssignedUser(user);
            task.setTeam(team);
            subtaskRepository.save(sub1);
            subtaskRepository.save(sub2);
            taskRepository.save(task);
        }

    }


    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testGetAllSubtasks() throws Exception {
        mockMvc.perform(get("/subtasks/get-all/{mainTaskId}", 1L)
                        .with(user("admin@admin.com").roles("ADMIN")))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").exists());

    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testGetAllSubtasks_NotFound() throws Exception {
        mockMvc.perform(get("/subtasks/get-all/{mainTaskId}", 999L)
                        .with(user("admin@admin.com").roles("ADMIN")))

                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testRemoveSubtask() throws Exception {
        mockMvc.perform(delete("/subtasks/remove/{id}", 2L)
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testRemoveSubtask_NotFound() throws Exception {
        mockMvc.perform(delete("/subtasks/remove/{id}", 999L)
                        .with(user("admin@admin.com").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    @AutoConfigureMockMvc(addFilters = false)
    public void testUpdateSubtask() throws Exception {
        String jsonRequest = """
                {
                    "id": 1,
                    "content": "Zadank111o Taras",
                    "status": false
                }
                """;


        mockMvc.perform(post("/subtasks/update")
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
    public void testUpdateSubtask_NotFound() throws Exception {
        String jsonRequest = """
                {
                    "id": 999,
                    "content": "Zadank111o Taras",
                    "status": false
                }
                """;

        mockMvc.perform(post("/subtasks/update")
                        .with(user("admin@admin.com").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isNotFound());
//                .andExpect(jsonPath("$.teamName").value("test team"))
//                .andExpect(jsonPath("$.description").value("test description"));
    }


}