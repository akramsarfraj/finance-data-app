package com.zorvyn.financedata.service;

import com.zorvyn.financedata.dto.AdminDto;
import com.zorvyn.financedata.dto.ResponseBody;
import com.zorvyn.financedata.dto.UserDto;
import com.zorvyn.financedata.exception.UserNotFoundException;
import com.zorvyn.financedata.model.User;
import com.zorvyn.financedata.repository.UserRepository;
import com.zorvyn.financedata.security.UserPrinciple;
import com.zorvyn.financedata.security.jwt.JwtService;
import com.zorvyn.financedata.util.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<ResponseBody<User>> createUser(UserDto userDto){
        log.info("Creating new user.");

        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());

        Set<Roles> roles = new HashSet<>();
        roles.add(Roles.getRole(userDto.getRole()));
        user.setRole(roles);

        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setRoleLabel(Roles.getRoleLabel(userDto.getRole()));
        user.setActive(true);
        user.setDelete(false);

        ResponseBody<User> body = new ResponseBody<>();
        body.setMessage("new user created.");
        body.setCurrentTimestamp(new Date());
        body.setStatus(HttpStatus.OK.getReasonPhrase());
        body.setData(userRepository.save(user));

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public ResponseEntity<ResponseBody<User>> updateUser(UserDto dto){

        log.info("Updating the user.");

        Optional<User> optional = userRepository.findById(dto.getId());

        if(optional.isPresent()){
            User dbUser = optional.get();
            dbUser.setEmail(dto.getEmail());
            dbUser.setUserName(dto.getUserName());


            ResponseBody<User> body = new ResponseBody<>();
            body.setMessage("user updated.");
            body.setCurrentTimestamp(new Date());
            body.setStatus(HttpStatus.OK.getReasonPhrase());
            body.setData(userRepository.save(dbUser));

            return ResponseEntity.status(HttpStatus.OK).body(body);
        }

        throw new UserNotFoundException("No user detail found for the id : "+dto.getId());
    }

    public ResponseEntity<ResponseBody<Page<User>>> getAllUsers(int pageSize, int pageNumber, Boolean isDeleted, Boolean isActive, String userName){

        Sort sort = Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<User> users = userRepository.findAllByActiveAndDeleteFilter(pageable,isDeleted,isActive,userName);

        ResponseBody<Page<User>> body = new ResponseBody<>();
        body.setMessage("all user list fetch.");
        body.setCurrentTimestamp(new Date());
        body.setStatus(HttpStatus.OK.getReasonPhrase());
        body.setData(users);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    public ResponseEntity<ResponseBody<User>> getUserById(Long id){
        Optional<User> optional = userRepository.findById(id);
        if(optional.isPresent()){

            ResponseBody<User> body = new ResponseBody<>();
            body.setMessage("user fetch.");
            body.setCurrentTimestamp(new Date());
            body.setStatus(HttpStatus.OK.getReasonPhrase());
            body.setData(optional.get());

            return ResponseEntity.status(HttpStatus.OK).body(body);
        }

        throw new UserNotFoundException("No user detail found for the id : "+id);
    }

    public ResponseEntity<ResponseBody> softDeleteUser(Long id){
        log.info("soft deleting the User");
        Optional<User> optional = userRepository.findById(id);
        if(optional.isPresent()){

            User user = optional.get();
            user.setDelete(true);
            user.setDeleteAt(LocalDateTime.now());

            userRepository.save(user);

            ResponseBody body = new ResponseBody<>();
            body.setMessage("user deleted. you can recover account in 14 day.");
            body.setCurrentTimestamp(new Date());
            body.setStatus(HttpStatus.OK.getReasonPhrase());
            body.setData(null);

            return ResponseEntity.status(HttpStatus.OK).body(body);
        }

        throw new UserNotFoundException("No user detail found for the id : "+id);
    }

    public ResponseEntity<ResponseBody> deleteUserPermanently(Long id){
        log.info("soft deleting the User");
        Optional<User> optional = userRepository.findById(id);
        if(optional.isPresent()){

            User user = optional.get();

            userRepository.delete(user);

            ResponseBody body = new ResponseBody<>();
            body.setMessage("user deleted permanently.");
            body.setCurrentTimestamp(new Date());
            body.setStatus(HttpStatus.OK.getReasonPhrase());
            body.setData(null);

            return ResponseEntity.status(HttpStatus.OK).body(body);
        }

        throw new UserNotFoundException("No user detail found for the id : "+id);
    }

    public ResponseEntity<ResponseBody> recoverDeleteUser(String id){
        log.info("recovering deleting the User");
        Optional<User> optional = userRepository.findByEmail(id);
        if(optional.isPresent()){

            User user = optional.get();

            user.setDelete(false);
            user.setDeleteAt(null);

            ResponseBody body = new ResponseBody<>();
            body.setMessage("user successfully recover.");
            body.setCurrentTimestamp(new Date());
            body.setStatus(HttpStatus.OK.getReasonPhrase());
            body.setData(userRepository.save(user));

            return ResponseEntity.status(HttpStatus.OK).body(body);
        }

        throw new UserNotFoundException("No user detail found for the id : "+id);
    }

    public ResponseEntity<ResponseBody> updateUserActiveStatus(boolean isActive, Long id){
        log.info("updating user status.");
        Optional<User> optional = userRepository.findById(id);
        if(optional.isPresent()){

            User user = optional.get();
            user.setActive(isActive);

            ResponseBody<User> body = new ResponseBody<>();
            body.setMessage("user deleted.");
            body.setCurrentTimestamp(new Date());
            body.setStatus(HttpStatus.OK.getReasonPhrase());
            body.setData(userRepository.save(user));

            return ResponseEntity.status(HttpStatus.OK).body(body);
        }

        throw new UserNotFoundException("No user detail found for the id : "+id);
    }

    public ResponseEntity<ResponseBody<User>> createAdmin(AdminDto dto) {
        log.info("Creating new user.");

        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());

        Set<Roles> roles = new HashSet<>();
        roles.add(Roles.ADMIN);
        user.setRole(roles);

        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRoleLabel(Roles.ADMIN.getLabel());
        user.setActive(true);
        user.setDelete(false);

        ResponseBody<User> body = new ResponseBody<>();
        body.setMessage("new user created.");
        body.setCurrentTimestamp(new Date());
        body.setStatus(HttpStatus.OK.getReasonPhrase());
        body.setData(userRepository.save(user));

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public ResponseEntity<ResponseBody> login(String email, String password) {

        Authentication authentication =
                manager.authenticate(new UsernamePasswordAuthenticationToken(email,
                        password));

        ResponseBody body = new ResponseBody<>();
        body.setCurrentTimestamp(new Date());


        //Checking if User is present in Database or Not
        if(authentication.isAuthenticated()){

           String token = jwtService.generateToken((UserPrinciple) authentication.getPrincipal());
           body.setMessage("Login success");
           body.setData(token);
           body.setStatus(HttpStatus.OK.getReasonPhrase());

           return ResponseEntity.status(HttpStatus.OK).body(body);
        }
        body.setMessage("Login fail");
        body.setData(null);
        body.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
