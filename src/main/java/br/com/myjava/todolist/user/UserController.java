package br.com.myjava.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Modificadores
 * public
 * private
 * protected = mesma estrutura do pacote tem acesso
 */

@RequestMapping("/users")
@RestController
public class UserController {

    /*
    o @Autowired é um injeção de dependência
    gerencia o ciclo de vida da minha interface
    faça o que for necessário para que eu consiga usar o userRepository
     */
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity create(@RequestBody UserModel userModel) throws Exception {
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null) {
            return ResponseEntity.status(400).body("User already exists");
        }

        //var passwordHash = BCrypt.hashpw(userModel.getPassword(), BCrypt.gensalt());
        var passwordHash = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHash);

        var createdUser = this.userRepository.save(userModel);
        return ResponseEntity.status(201).body(createdUser);
    }

}
