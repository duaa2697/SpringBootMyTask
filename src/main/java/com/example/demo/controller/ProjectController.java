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
import com.example.demo.model.Project;
import com.example.demo.repository.ProjectRepository;

@RestController
@RequestMapping("/api")
public class ProjectController {

	@Autowired
	private ProjectRepository projectRepository;

	@GetMapping("/projects")
	public List<Project> getAllProject() {
		return projectRepository.findAll();
	}

	@GetMapping("/projects/{id}")
	public Project getProjectByID(@PathVariable Long id) {
		Optional<Project> optProject = projectRepository.findById(id);
		if (optProject.isPresent()) {
			return optProject.get();
		} else {
			throw new NotFoundException("Project not found with id " + id);
		}
	}

	@PostMapping("/projects")
	public Project createProject(@Valid @RequestBody Project project) {
		return projectRepository.save(project);
	}

	@PutMapping("/projects/{id}")
	public Project updateProject(@PathVariable Long id, @Valid @RequestBody Project projectUpdated) {
		return projectRepository.findById(id).map(project -> {
			project.setName(projectUpdated.getName());
			project.setDetails(projectUpdated.getDetails());
			return projectRepository.save(project);
		}).orElseThrow(() -> new NotFoundException("Project not found with id " + id));
	}

	@DeleteMapping("/projects/{id}")
	public String deleteProject(@PathVariable Long id) {
		return projectRepository.findById(id).map(project -> {
			projectRepository.delete(project);
			return "Delete OK";
		}).orElseThrow(() -> new NotFoundException("Project not found with id " + id));
	}

	@PostMapping("/projects/archive/{id}")
	public String archiveProject(@PathVariable Long id) {
		return projectRepository.findById(id).map(project -> {
			projectRepository.save(project);
			return "Archive OK";
		}).orElseThrow(() -> new NotFoundException("Project not found with id " + id));

	}

	@GetMapping("/projects/archive/{id}")
	public Project restoreProjectFromArchive(@PathVariable Long id) {
		Optional<Project> project = projectRepository.findById(id);
		return project.get();
	}

}