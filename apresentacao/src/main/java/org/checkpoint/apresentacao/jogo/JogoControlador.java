package org.checkpoint.apresentacao.jogo;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.checkpoint.dominio.jogo.*;
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
@RequestMapping("/api/jogo")
public class JogoControlador {
    private final Gson gson = new Gson();
    private @Autowired JogoServico jogoServico;
    private @Autowired AuthUtil authUtil;

    @GetMapping()
    public ResponseEntity<?> listJogos(HttpServletRequest request) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            List<Jogo> jogos = jogoServico.listJogos();
            return ResponseEntity.ok(jogos);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listJogos(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            Jogo jogo = jogoServico.getJogo(new JogoId(id));
            return ResponseEntity.ok(jogo);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping("/{id}/requisitos")
    public ResponseEntity<?> listRequisitos(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            List<RequisitosDeSistema> requisitosDeSistema = jogoServico.getRequisitosDeSistema(new JogoId(id));
            return ResponseEntity.ok(requisitosDeSistema);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> toogleGameLike(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            jogoServico.toggleGameLike(authenticatedUser, new JogoId(id));
            map.put("message", "sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/{id}/tag")
    public ResponseEntity<?> addTag(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody AddTagDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            jogoServico.addTagsToGame(new JogoId(id), authenticatedUser.getUserId(), data.tags());
            map.put("message", "sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping("/{id}/tag")
    public ResponseEntity<?> getTopTags(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            List<Tag> tags = jogoServico.getTopTags(new JogoId(id));

            return ResponseEntity.ok(tags);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @DeleteMapping("/{id}/tag")
    public ResponseEntity<?> removeTag(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody RemoveTagDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            jogoServico.removeTagFromGame(new JogoId(id), data.name(), authenticatedUser.getUserId());
            map.put("message", "sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/{id}/avaliacao")
    public ResponseEntity<?> addAvaliacao(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody AvaliacaoDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            jogoServico.submitAvaliacao(authenticatedUser, new JogoId(id), data.nota(), data.critica());
            map.put("message", "Avaliacao enviada com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PutMapping("/{id}/avaliacao/{avaliacaoId}")
    public ResponseEntity<?> editAvaliacao(
            HttpServletRequest request,
            @PathVariable("id") Integer id,
            @PathVariable("avaliacaoId") Integer avaliacaoId,
            @RequestBody AvaliacaoDTO data
    ) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            jogoServico.editAvaliacao(authenticatedUser, new AvaliacaoId(avaliacaoId), data.nota(), data.critica());
            map.put("message", "Avaliacao atualizada com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PatchMapping("/{id}/avaliacao/{avaliacaoId}")
    public ResponseEntity<?> toggleAvaliacaoLike(
            HttpServletRequest request,
            @PathVariable("id") Integer id,
            @PathVariable("avaliacaoId") Integer avaliacaoId
    ) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            jogoServico.toggleAvaliacaoLike(authenticatedUser, new AvaliacaoId(avaliacaoId));
            map.put("message", "sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }
}
