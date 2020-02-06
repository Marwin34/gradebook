package pl.edu.agh.zarzecze.gradebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.zarzecze.gradebook.model.Parent;
import pl.edu.agh.zarzecze.gradebook.repository.ParentRepository;


@RestController
public class ParentController {
    @Autowired
    private ParentRepository parentRepository;

    @GetMapping("/parents")
    public Iterable<Parent> getAllParents(){
        return parentRepository.findAll();
    }
}

