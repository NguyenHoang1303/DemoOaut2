package com.example.oauth2core.controller;

import com.example.oauth2core.dto.ClientDto;
import com.example.oauth2core.entity.ClientApp;
import com.example.oauth2core.repository.ClientAppRepository;
import com.example.oauth2core.responseapi.ResponseApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/clientApps")
@CrossOrigin(origins = "*")
public class ClientAppController {

    @Autowired
    ClientAppRepository clientAppRepository;

    public ResponseEntity createApp(ClientApp app){
        try {
            return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withHttpCode(200)
                    .withMessage("Ok")
                    .withData(clientAppRepository.save(app))
                    .build(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("not found")
                    .withHttpCode(404)
                    .build(), HttpStatus.FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ClientDto getApp(@RequestParam(name = "client_id") String clientId){
        return new ClientDto(clientAppRepository.findClientAppByClientId(clientId));
    }

}
