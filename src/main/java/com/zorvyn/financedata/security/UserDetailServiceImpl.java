package com.zorvyn.financedata.security;


import com.zorvyn.financedata.model.User;
import com.zorvyn.financedata.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optional = userRepository.findByEmailAndDelete(username);

        if(optional.isPresent()){
            User user = optional.get();
            return new UserPrinciple(user.getId(),
                    user.getUserName(),user.getEmail(),
                    user.getRoleLabel(),user.getRole(),user.getPassword());
        }

        throw  new UsernameNotFoundException("user not found.");
    }
}
