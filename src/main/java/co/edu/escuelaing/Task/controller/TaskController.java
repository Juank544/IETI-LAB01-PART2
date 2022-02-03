package co.edu.escuelaing.Task.controller;

import co.edu.escuelaing.Task.data.Task;
import co.edu.escuelaing.Task.dto.TaskDto;
import co.edu.escuelaing.Task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/v1/task")
public class TaskController {
    private final TaskService taskService;
    private final AtomicInteger count = new AtomicInteger(0);
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");


    public TaskController(@Autowired TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAll(){
        try {
            return new ResponseEntity<>(taskService.getAll(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE,null,e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable String id){
        try {
            return new ResponseEntity<>(taskService.findById(id),HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE,null,e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody TaskDto taskDto){
        try {
            LocalDateTime now = LocalDateTime.now();
            Task task = new Task();
            task.setId(String.valueOf(count.incrementAndGet()));
            task.setName(taskDto.getName());
            task.setDescription(taskDto.getDescription());
            task.setStatus(taskDto.getStatus());
            task.setAssignedTo(taskDto.getAssignedTo());
            task.setDueDate(taskDto.getDueDate());
            task.setCreated(dtf.format(now));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE,null,e);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@RequestBody TaskDto taskDto, @PathVariable String id){
        try {
            Task task = taskService.findById(id);
            task.setName(taskDto.getName());
            task.setDescription(taskDto.getDescription());
            task.setStatus(taskDto.getStatus());
            task.setAssignedTo(taskDto.getAssignedTo());
            taskService.update(task,id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE,null,e);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id){
        try {
            taskService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            Logger.getLogger(TaskController.class.getName()).log(Level.SEVERE,null,e);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
