package br.com.myjava.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/*
Representação (contrato) dos métodos que serão implementados
< > -> pega parÂmetros gerais tipo: T, L, ID...
 */
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    /*
    Finder automático possível por conta do springDaTA
     */
    UserModel findByUsername(String username);

}
