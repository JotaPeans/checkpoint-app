package org.checkpoint.apresentacao.user;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.checkpoint.dominio.email.Token;
import org.checkpoint.dominio.jogo.Jogo;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.jogo.JogoServico;
import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.user.UserId;
import org.checkpoint.dominio.user.UserServico;
import org.checkpoint.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/user")
public class UserControlador {
    private @Autowired UserServico userServico;

    private @Autowired JogoServico jogoServico;

    private @Autowired AuthUtil authUtil;

    private final Gson gson = new Gson();

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarUser(HttpServletRequest request, @RequestBody UserDTO data) {
        Map<String, Object> map = new HashMap<>();

        try {
            String message = userServico.registerUser(data.email(), data.senha(), data.nome());
            map.put("message", message);
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(HttpServletRequest request, @RequestBody LoginUserDTO data, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();

        try {
            String jwtToken = userServico.login(data.email(), data.senha());
            map.put("token", jwtToken);
            String json = gson.toJson(map);

            //! Sem SAME_SITE... Precisa?
            Cookie cookie = new Cookie("token", jwtToken);
            cookie.setHttpOnly(true); // não acessível via JS (protege contra XSS)
            cookie.setSecure(true); // exige HTTPS
            cookie.setPath("/"); // cookie enviado para toda a aplicação
            cookie.setMaxAge(60 * 60 * 24 * 7); // 7 dias

            response.addCookie(cookie);

            return ResponseEntity.ok(json);
        } catch (Exception e) {

            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true); // não acessível via JS (protege contra XSS)
        cookie.setSecure(true); // exige HTTPS
        cookie.setPath("/"); // cookie enviado para toda a aplicação

        response.addCookie(cookie);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/verify-email/{token}")
    public Object verifyUserEmail(@PathVariable("token") String token) {
        Map<String, Object> map = new HashMap<>();

        try {
            userServico.verifyUserEmail(new Token(token));

            map.put("message", "Email verificado com sucesso");
            String json = gson.toJson(map);

            return new RedirectView("http://localhost:3000/login");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getAuthenticatedUser(HttpServletRequest request) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            User user = userServico.getUserById(authenticatedUser.getUserId());

            user.setSenha("");

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<String> atualizarUser(HttpServletRequest request, @RequestBody UpdateUserDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            userServico.updateProfile(authenticatedUser, data.nome(), data.bio(), data.redesSociais());
            map.put("message", "Usuario atualizado com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {

            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PatchMapping("/avatar")
    public ResponseEntity<String> atualizarUserAvatar(HttpServletRequest request, @RequestBody UpdateAvatarUserDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            userServico.updateAvatar(authenticatedUser, data.avatarUrl());
            map.put("message", "Avatar do usuario atualizado com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {

            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PatchMapping("/privacidade")
    public ResponseEntity<String> atualizarUserPrivacidade(HttpServletRequest request, @RequestBody UpdatePrivacidadeUserDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            userServico.togglePrivacidade(authenticatedUser, data.isPrivate());
            map.put("message", "Privacidade do usuario atualizado com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            User user = userServico.getInformacoes(authenticatedUser, new UserId(id));

            if(user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            user.setSenha("");

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            map.put("message", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/toggle-follow/{id}")
    public ResponseEntity<String> followUser(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            userServico.toggleSeguir(authenticatedUser, new UserId(id));
            map.put("message", "Ação feita com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {

            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/approve-follow/{id}")
    public ResponseEntity<String> approveFollow(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            userServico.approveSeguidor(authenticatedUser, new UserId(id));
            map.put("message", "Ação feita com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {

            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/reject-follow/{id}")
    public ResponseEntity<String> rejectFollow(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            userServico.rejectSeguidor(authenticatedUser, new UserId(id));
            map.put("message", "Ação feita com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {

            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/jogo-favorito/{id}")
    public ResponseEntity<String> addJogoFavorito(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();


        try {
            Jogo jogo = jogoServico.getJogo(new JogoId(id));
            userServico.addJogoFavorito(authenticatedUser, jogo);

            map.put("message", "Jogo adicionado aos favoritos com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("message", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @DeleteMapping("/jogo-favorito/{id}")
    public ResponseEntity<String> removeJogoFavorito(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            Jogo jogo = jogoServico.getJogo(new JogoId(id));
            userServico.removeJogoFavorito(authenticatedUser, jogo);

            map.put("message", "Jogo removido dos favoritos com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("message", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

}
