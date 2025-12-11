package org.checkpoint.apresentacao.lista;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.jogo.JogoServico;
import org.checkpoint.dominio.lista.ListaId;
import org.checkpoint.dominio.lista.ListaJogos;
import org.checkpoint.dominio.lista.ListaServico;
import org.checkpoint.dominio.user.User;
import org.checkpoint.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lista")
public class ListaControlador {
    private final Gson gson = new Gson();
    private @Autowired ListaServico listaServico;
    private @Autowired JogoServico jogoServico;
    private @Autowired AuthUtil authUtil;

    @PostMapping()
    public ResponseEntity<?> createLista(HttpServletRequest request, @RequestBody ListaDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            listaServico.createLista(authenticatedUser, data.titulo(), data.isPrivate());
            map.put("message", "Lista criada com sucesso!");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getListas(HttpServletRequest request) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            List<ListaJogos> listas = listaServico.listListasByUser(authenticatedUser);

            return ResponseEntity.ok(listas);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLista(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            ListaJogos lista = listaServico.getListaById(authenticatedUser, new ListaId(id));

            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLista(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            listaServico.deleteLista(authenticatedUser, new ListaId(id));
            map.put("message", "Lista excluída com sucesso!");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping("/list/public")
    public ResponseEntity<?> getListasPublicas(HttpServletRequest request) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            List<ListaJogos> listas = listaServico.getListasPublicas(authenticatedUser);

            return ResponseEntity.ok(listas);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping("/list/public/paginated")
    public ResponseEntity<?> getListasPublicasPaginadas(
            HttpServletRequest request,
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="size", defaultValue = "10") int size) {

        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> response = new HashMap<>();

        try {
            // Obtém as listas paginadas
            List<ListaJogos> listas = listaServico.getListasPublicasPaginadas(
                    authenticatedUser,
                    page,
                    size
            );

            // Obtém informações de paginação
            ListaServico.PaginationInfo paginationInfo =
                    listaServico.getListasPublicasPaginationInfo(authenticatedUser, size);

            // Monta a resposta com dados e metadados de paginação
            response.put("data", listas);
            response.put("pagination", Map.of(
                    "currentPage", page,
                    "pageSize", size,
                    "totalItems", paginationInfo.getTotalItems(),
                    "totalPages", paginationInfo.getTotalPages(),
                    "hasNext", page < paginationInfo.getTotalPages() - 1,
                    "hasPrevious", page > 0
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            String json = gson.toJson(response);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @GetMapping("/list/public/iterator")
    public ResponseEntity<?> getListasPublicasComIterator(
            HttpServletRequest request,
            @RequestParam(defaultValue = "10") int pageSize) {

        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> response = new HashMap<>();

        try {
            // Cria o iterator
            ListaServico.ListasPublicasIterator iterator =
                    listaServico.getListasPublicasIterator(authenticatedUser, pageSize);

            // Retorna informações do iterator e a primeira página
            response.put("firstPage", iterator.nextPage());
            response.put("info", Map.of(
                    "totalItems", iterator.getTotalItems(),
                    "totalPages", iterator.getTotalPages(),
                    "pageSize", pageSize,
                    "hasNextPage", iterator.hasNextPage()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            String json = gson.toJson(response);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PatchMapping("/{id}/jogo")
    public ResponseEntity<?> addJogoToLista(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody UpdateJogoListaDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        List<JogoId> jogos = new ArrayList<>();

        for (Integer jogoIdInt : data.jogosIds()) {
            jogos.add(new JogoId(jogoIdInt));
        }

        try {
            listaServico.updateJogos(authenticatedUser, new ListaId(id), jogos);
            map.put("message", "Jogo adicionado com sucesso com sucesso!");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/{id}/duplicate")
    public ResponseEntity<?> duplicateLista(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            listaServico.duplicateLista(authenticatedUser, new ListaId(id));
            map.put("message", "Lista copiada com sucesso!");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PatchMapping("/{id}/titulo")
    public ResponseEntity<?> updateTitulo(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody UpdateTituloListaDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            listaServico.updateTitulo(authenticatedUser, new ListaId(id), data.titulo());
            map.put("message", "Titulo da lista atualizado com sucesso!");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PatchMapping("/{id}/privacidade")
    public ResponseEntity<?> updatePrivacidade(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody UpdatePrivacidadeListaDTO data) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            listaServico.togglePrivacidade(authenticatedUser, new ListaId(id), data.isPrivate());
            map.put("message", "Privacidade da lista atualizado com sucesso!");
            String json = gson.toJson(map);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            String json = gson.toJson(map);
            return ResponseEntity.badRequest().body(json);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> toggleLikeLista(HttpServletRequest request, @PathVariable("id") Integer id) {
        User authenticatedUser = authUtil.getAuth(request);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, Object> map = new HashMap<>();

        try {
            listaServico.toggleListaLike(authenticatedUser, new ListaId(id));
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