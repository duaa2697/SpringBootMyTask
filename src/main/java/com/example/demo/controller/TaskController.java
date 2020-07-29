package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Task;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;

@RestController
@RequestMapping("/api")
public class TaskController {
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@GetMapping("/projects/{id}/tasks")
	public List<Task> getAllTasksProject(@PathVariable Long id) {

		if (!projectRepository.existsById(id)) {
			throw new NotFoundException("Project not found!");
		}

		return taskRepository.findByProjectId(id);
	}

	@PostMapping("/projects/{id}/tasks")
	public Task addTask(@PathVariable Long id, @Valid @RequestBody Task task) {
		return projectRepository.findById(id).map(project -> {
			task.setProject(project);
			return taskRepository.save(task);
		}).orElseThrow(() -> new NotFoundException("Project not found!"));
	}

	@GetMapping("/projects/{projectId}/tasks/{taskId}")
	public Optional<Task> getTask(@PathVariable Long projectId, @PathVariable Long taskId) {

		if (!projectRepository.existsById(projectId)) {
			throw new NotFoundException("Project not found!");
		}

		return taskRepository.findById(taskId);
	}

	@PutMapping("/projects/{projectId}/tasks/{taskId}")
	public Task updateTask(@PathVariable Long projectId, @PathVariable Long taskId,
			@Valid @RequestBody Task taskUpdated) {

		if (!projectRepository.existsById(projectId)) {
			throw new NotFoundException("Project not found!");
		}

		return taskRepository.findById(taskId).map(task -> {
			task.setDetails(taskUpdated.getDetails());
			task.setIsComplete(taskUpdated.getIsComplete());
			return taskRepository.save(task);
		}).orElseThrow(() -> new NotFoundException("Task not found!"));
	}

	@DeleteMapping("/projects/{projectId}/tasks/{taskId}")
	public String deleteTask(@PathVariable Long projectId, @PathVariable Long taskId) {

		if (!projectRepository.existsById(projectId)) {
			throw new NotFoundException("Project not found!");
		}

		return taskRepository.findById(taskId).map(task -> {
			taskRepository.delete(task);
			return "Deleted Successfully!";
		}).orElseThrow(() -> new NotFoundException("Task not found!"));
	}
}
