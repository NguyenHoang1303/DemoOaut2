package com.example.oauth2core.controller;

import com.example.oauth2core.entity.AuthorCode;
import com.example.oauth2core.entity.ClientApp;
import com.example.oauth2core.entity.TokenApp;
import com.example.oauth2core.entity.User;
import com.example.oauth2core.repository.*;
import com.example.oauth2core.responseapi.ResponseApi;
import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/v1/auth")
@Log4j2
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClientAppRepository clientAppRepository;

    @Autowired
    AuthorCodeRepository authorCodeRepository;

    @Autowired
    ScopeRepository scopeRepository;

    @Autowired
    TokenAppRepository tokenAppRepository;

    private final HashMap<String, String> keyLogin = new HashMap<>();

    @RequestMapping(method = RequestMethod.POST, path = "register")
    public String register(@RequestParam(name = "username") String username,
                           @RequestParam(name = "password") String password

    ) {
        try {
            User user = userRepository.findUserByUsername(username);
            if (user != null) {
                throw new RuntimeException("tài khoản tồn tại");
            }
            userRepository.save(new User(username, BCrypt.hashpw(password, BCrypt.gensalt(10))));
            return "success";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "login")
    public ResponseEntity loginOauth2(@RequestParam(name = "username") String username,
                                      @RequestParam(name = "password") String password
    ) {
        try {
            if (username == null || password == null) {
                return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                        .aResponseApi()
                        .withMessage("Vui lòng nhập thông tin tài khoản")
                        .withHttpCode(400)
                        .build(), HttpStatus.BAD_REQUEST);
            }
            User user = userRepository.findUserByUsername(username);
            if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
                return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                        .aResponseApi()
                        .withMessage("kiểm tra thông tin tài khoản")
                        .withHttpCode(400)
                        .build(), HttpStatus.BAD_REQUEST);
            }
            String key = UUID.randomUUID().toString();
            keyLogin.put(key, username);

            return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Ok")
                    .withHttpCode(200)
                    .withData(key)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Ok")
                    .withHttpCode(400)
                    .build(), HttpStatus.FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "check-login")
    public boolean checkLogin(@RequestParam String key){
        return keyLogin.containsKey(key);
    }

    @RequestMapping(method = RequestMethod.POST, path = "author-code")
    public ResponseEntity getAuthorCode(
            @RequestParam(name = "key") String key,
            @RequestParam(name = "redirect_uri") String redirectUri,
            @RequestParam(name = "client_id") String clientId,
            @RequestParam(name = "scope") String scope) {
        if (!keyLogin.containsKey(key)) {
            return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Lỗi")
                    .withHttpCode(403)
                    .build(), HttpStatus.FORBIDDEN);
        }

        ClientApp clientApp = clientAppRepository.findClientAppByClientId(clientId);
        log.info("sssssssssss: " + clientId);
        if (clientApp == null) {
            return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Lỗi clientApp")
                    .withHttpCode(400)
                    .build(), HttpStatus.FOUND);
        }
        if (!clientApp.getRootUrl().equals(redirectUri)) {
            return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Lỗi UrlRedirect")
                    .withHttpCode(400)
                    .build(), HttpStatus.FOUND);
        }

        User user = clientApp.getUser();
        if (user == null) {
            throw new RuntimeException("Kiểm tra lại thông tin");
        }
        String code = UUID.randomUUID().toString();
        authorCodeRepository.save(new AuthorCode(clientId, code, scope));

        return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                .aResponseApi()
                .withAuthorCode(code)
                .build(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "token")
    public ResponseEntity getToken(@RequestParam(name = "redirect_uri") String redirectUri,
                                   @RequestParam(name = "client_id") String clientId,
                                   @RequestParam(name = "secret_id") String secretId,
                                   @RequestParam(name = "code") String code
    ) {

        AuthorCode authorCode = authorCodeRepository.findAuthorCodeByAuthorizationCode(code);
        if (authorCode == null) {
            new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("code ko tồn tại")
                    .withHttpCode(400)
                    .build(), HttpStatus.FOUND);
        }
        if (!authorCode.getClientId().equals(clientId)) {
            new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("clientId ko tồn tại")
                    .withHttpCode(400)
                    .build(), HttpStatus.FOUND);
        }

        ClientApp clientApp = clientAppRepository.findClientAppByClientId(clientId);
        if (!clientApp.getSecretId().equals(secretId)) {
            new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("secretId ko tồn tại")
                    .withHttpCode(400)
                    .build(), HttpStatus.FOUND);
        }

        if (!clientApp.getRootUrl().equals(redirectUri)) {
            new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("redirectUri ko tồn tại")
                    .withHttpCode(400)
                    .build(), HttpStatus.FOUND);
        }


        String token = UUID.randomUUID().toString();
        tokenAppRepository.save(TokenApp.TokenAppBuilder.aTokenApp()
                .withAccessToken(token)
                .withClientId(clientId)
                .build());
        return new ResponseEntity<>(ResponseApi.ResponseApiBuilder
                .aResponseApi()
                .withToken(token)
                .build(), HttpStatus.OK);
    }

}
