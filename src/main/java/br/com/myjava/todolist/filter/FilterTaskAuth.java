package br.com.myjava.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.myjava.todolist.user.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;


@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.equals("/tasks/")) {
            //Pegar a autenticação (usuário e senha)
            var authorization = request.getHeader("Authorization");
            //respósta em base64. exemplo: Basic YWRtaW46YWRtaW4=
            // precisamos decodificar

            //pega apenas a string decodificada
            var authBase64 = authorization.substring("Basic".length()).trim();
            byte[] authBytes = Base64.getDecoder().decode(authBase64);
            String authString = new String(authBytes); //nos retorna usuario:senha

            String[] usernamePassword = authString.split(":");
            String username = usernamePassword[0];
            String password = usernamePassword[1];

            //Validar se o usuário existe
            var user = this.userRepository.findByUsername(username);

            if (user == null) {
                response.sendError(401, "User not found");
            } else {
                //Validar senha
                var result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (result.verified) {
                    request.setAttribute("userId", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401, "Unauthorized");
                }

            }
        }
    }
}

/*
Classe que implementa o filtro de autenticação
o que é servlet: base para qualquer framework web
component: manda pro spring gerenciar esta classe, que nem o restController
 */
//@Component
//public class FilterTaskAuth implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response,
//                         FilterChain chain) throws IOException, ServletException {
//
//        chain.doFilter(request, response);
//
//    }
//}
