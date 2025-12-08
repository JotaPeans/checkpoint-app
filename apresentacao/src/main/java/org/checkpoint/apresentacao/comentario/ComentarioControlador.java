package org.checkpoint.apresentacao.comentario;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.checkpoint.dominio.comentario.Comentario;
import org.checkpoint.dominio.comentario.ComentarioId;
import org.checkpoint.dominio.comentario.ComentarioServico;
import org.checkpoint.dominio.jogo.AvaliacaoId;
import org.checkpoint.dominio.lista.ListaId;
import org.checkpoint.dominio.user.User;
import org.checkpoint.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comentario")
public class ComentarioControlador {
    private final Gson gson = new Gson();
    private @Autowired ComentarioServico comentarioServico;
    private @Autowired AuthUtil authUtil;

    @GetMapping("/avaliacao/{id}")
    public ResponseEntity<?> listComentariosByAvaliacao(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            List<Comentario> comentarios = this.comentarioServico.listComentariosByAvaliacaoAlvo(new AvaliacaoId(id));

            return ResponseEntity.ok(comentarios);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping("/lista/{id}")
    public ResponseEntity<?> listComentariosByLista(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            List<Comentario> comentarios = this.comentarioServico.listComentariosByListaAlvo(new ListaId(id));

            return ResponseEntity.ok(comentarios);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping("/comentario-pai/{id}")
    public ResponseEntity<?> listComentariosByComentarioPai(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            List<Comentario> comentarios = this.comentarioServico.listComentariosByPai(new ComentarioId(id));

            return ResponseEntity.ok(comentarios);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/avaliacao/{id}")
    public ResponseEntity<?> createComentarioByAvaliacao(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody ComentarioDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            this.comentarioServico.addComentarioAvaliacaoRaiz(authenticatedUser, new AvaliacaoId(id), data.text());
            map.put("message", "Comentario adicionado com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/lista/{id}")
    public ResponseEntity<?> createComentarioByLista(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody ComentarioDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            this.comentarioServico.addComentarioListaRaiz(authenticatedUser, new ListaId(id), data.text());
            map.put("message", "Comentario adicionado com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/comentario-pai/{id}")
    public ResponseEntity<?> createComentarioByComentario(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody ComentarioDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            this.comentarioServico.replyComentario(authenticatedUser, new ComentarioId(id), data.text());
            map.put("message", "Comentario adicionado com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editComentario(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody ComentarioDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            this.comentarioServico.editComentario(authenticatedUser, new ComentarioId(id), data.text());
            map.put("message", "Comentario alterado com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleComentarioLike(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            this.comentarioServico.toggleComentarioLike(authenticatedUser, new ComentarioId(id));
            map.put("message", "sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComentario(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            this.comentarioServico.deleteComentario(authenticatedUser, new ComentarioId(id));
            map.put("message", "Comentario excluído com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }
}
