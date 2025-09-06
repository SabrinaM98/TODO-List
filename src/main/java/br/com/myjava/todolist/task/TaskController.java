package br.com.myjava.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        TaskModel task = null;
        try {
            taskModel.setUserId((UUID) request.getAttribute("userId"));
            var currentTime = LocalDateTime.now();
            taskModel.setCreateDate(currentTime);
            task = this.taskRepository.save(taskModel);
        } catch (Exception e) {
            ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(201).body(task);
    }
}
