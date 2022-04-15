package com.example.oauth2core.controller;

import com.example.oauth2core.entity.Scope;
import com.example.oauth2core.repository.ScopeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/scopes")
@Log4j2
@CrossOrigin(origins = "*")
public class ScopeController {

    @Autowired
    ScopeRepository scopeRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Scope getScopeByName(String name){
        return scopeRepository.findByName(name);
    }
}
