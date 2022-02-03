package co.edu.escuelaing.Task.service.impl;

import co.edu.escuelaing.Task.data.Task;
import co.edu.escuelaing.Task.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskImpl implements TaskService {
    private final ConcurrentHashMap<String,Task> tasks = new ConcurrentHashMap<>();

    @Override
    public Task create(Task newTask) {
        Task task = tasks.put(newTask.getId(),newTask);
        return task;
    }

    @Override
    public Task findById(String id) {
        Task taskReturn = null;
        for (Map.Entry<String,Task> entry : tasks.entrySet()){
            if (entry.getKey().equals(id)){
                taskReturn = entry.getValue();
            }
        }
        return taskReturn;
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasksReturn = new ArrayList<>();
        for (Map.Entry<String,Task> entry : tasks.entrySet()){
            tasksReturn.add(entry.getValue());
        }
        return tasksReturn;
    }

    @Override
    public boolean deleteById(String id) {
        for (Map.Entry<String,Task> entry : tasks.entrySet()){
            if (entry.getKey().equals(id)){
                tasks.remove(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public Task update(Task newTask, String id) {
        Task task = tasks.putIfAbsent(newTask.getId(),newTask);
        if (task!=null){
            tasks.replace(newTask.getId(),newTask);
        }
        return task;
    }
}
