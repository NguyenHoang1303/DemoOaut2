package com.example.oauth2core.controller;

import com.example.oauth2core.dto.Credential;
import com.example.oauth2core.dto.FormUser;
import com.example.oauth2core.entity.AuthorCode;
import com.example.oauth2core.entity.User;
import com.example.oauth2core.entity.ClientApp;
import com.example.oauth2core.repository.AuthorCodeRepository;
import com.example.oauth2core.repository.UserRepository;
import com.example.oauth2core.repository.ClientAppRepository;
import com.example.oauth2core.responseapi.ResponseApi;
import com.example.oauth2core.util.JwtHandler;
import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
@Log4j2
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClientAppRepository clientAppRepository;

    @Autowired
    AuthorCodeRepository authorCodeRepository;

    @Autowired
    JwtHandler jwtHandler;

    @RequestMapping(method = RequestMethod.POST, path = "register")
    public String register(FormUser formUser) {
        try {
            User user = userRepository.findUserByUsername(formUser.getUsername());
            if (user != null) {
                throw new RuntimeException("tài khoản tồn tại");
            }
            formUser.setPassword(BCrypt.hashpw(formUser.getPassword(), BCrypt.gensalt(10)));
            userRepository.save(new User(formUser));
            return "success";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "login")
    public ResponseEntity loginOauth2(@RequestParam(name = "username") String username,
                                      @RequestParam(name = "password") String password) {
        try {

            if (username == null || password == null) {
                new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                        .aResponseApi()
                        .withMessage("Vui lòng nhập thông tin tài khoản")
                        .withHttpCode(400)
                        .build(), HttpStatus.BAD_REQUEST);
            }
            User user = userRepository.findUserByUsername(username);
            if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
                new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                        .aResponseApi()
                        .withMessage("kiểm tra thông tin tài khoản")
                        .withHttpCode(400)
                        .build(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Ok")
                    .withHttpCode(200)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Ok")
                    .withHttpCode(200)
                    .build(), HttpStatus.FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "author-code")
    public ResponseEntity submitConsentForm(@RequestParam(name = "redirect_uri") String redirectUri,
                                            @RequestParam(name = "client_id") String clientId,
                                            @RequestParam(name = "scope") String scope,
                                            @RequestParam(name = "username") String username
    ) {

        ClientApp clientApp = clientAppRepository.findWebAppByClientId(clientId);
        if (clientApp == null) {
            return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Lỗi")
                    .withHttpCode(400)
                    .build(), HttpStatus.FOUND);
        }
        if (!clientApp.getUrl().equals(redirectUri)) {
            return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Lỗi")
                    .withHttpCode(400)
                    .build(), HttpStatus.FOUND);
        }

        User user = clientApp.getUser();
        if (user == null) {
            throw new RuntimeException("Kiểm tra lại thông tin");
        }
        String code = UUID.randomUUID().toString();
        authorCodeRepository.save(new AuthorCode(clientId, code, scope, username));

        return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                .aResponseApi()
                .withAuthorCode(code)
                .build(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "decode-token")
    public Credential getToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            jwtHandler.decodeToken(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Credential();
    }


}
