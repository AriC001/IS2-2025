package com.example.etemplate.servicios;

import com.example.etemplate.entities.Usuario;
import com.example.etemplate.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericServiceImpl<Usuario, String> {

    private final UserRepository userRepository;

    public UserService(UserRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    public Usuario findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}

