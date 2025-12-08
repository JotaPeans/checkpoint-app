package org.checkpoint.apresentacao.diario;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.checkpoint.dominio.diario.*;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.jogo.JogoServico;
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
@RequestMapping("/api/diario")
public class DiarioControlador {
    private @Autowired JogoServico jogoServico;

    private @Autowired DiarioServico diarioServico;

    private @Autowired AuthUtil authUtil;

    private final Gson gson = new Gson();

    @GetMapping()
    public ResponseEntity<?> getDiario(HttpServletRequest request) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            Diario diario = diarioServico.getDiario(authenticatedUser);
            return ResponseEntity.ok(diario);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> addRegistro(HttpServletRequest request, @RequestBody AddRegistroDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            diarioServico.addRegistro(authenticatedUser, new JogoId(data.jogoId()), data.dataInicio(), data.dataTermino());

            map.put("message", "Registro adicionado com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping("/registro")
    public ResponseEntity<?> listRegistros(HttpServletRequest request) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            List<RegistroDiario> registros = diarioServico.listRegistros(authenticatedUser);

            return ResponseEntity.ok(registros);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PutMapping("/registro/{id}")
    public ResponseEntity<?> updateRegistro(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody UpdateRegistroDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            diarioServico.updateRegistro(new RegistroId(id), data.dataInicio(), data.dataTermino());

            map.put("message", "Registro atualizado com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @DeleteMapping("/registro/{id}")
    public ResponseEntity<?> DeleteRegistro(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            diarioServico.removeRegistro(authenticatedUser, new RegistroId(id));

            map.put("message", "Registro removido com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/conquista")
    public ResponseEntity<?> addConquista(HttpServletRequest request, @RequestBody AddConquistaDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            diarioServico.addConquista(new RegistroId(data.registroId()), data.nome(), data.dataDesbloqueada(), data.concluida());

            map.put("message", "Conquista adicionada com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PutMapping("/conquista/{id}")
    public ResponseEntity<?> addConquista(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody UpdateConquistaDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            diarioServico.updateConquista(new ConquistaId(id), data.nome(), data.dataDesbloqueada(), data.concluida());

            map.put("message", "Conquista atualizada com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping("/registro/{registroId}/conquista")
    public ResponseEntity<?> listConquistas(HttpServletRequest request, @PathVariable("registroId") Integer registroId) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            List<Conquista> registros = diarioServico.listConquistas(authenticatedUser, new RegistroId(registroId));

            return ResponseEntity.ok(registros);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @DeleteMapping("/registro/{registroId}/conquista/{conquistaId}")
    public ResponseEntity<?> removeConquista(HttpServletRequest request, @PathVariable("registroId") Integer registroId, @PathVariable("conquistaId") Integer conquistaId) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            diarioServico.removeConquista(authenticatedUser, new RegistroId(registroId), new ConquistaId(conquistaId));

            map.put("message", "Conquista removida com sucesso");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }
}
