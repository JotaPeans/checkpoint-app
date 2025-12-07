package org.checkpoint.persistencia.jpa;

import java.util.ArrayList;
import java.util.List;

import org.checkpoint.dominio.comentario.Comentario;
import org.checkpoint.dominio.comentario.ComentarioId;
import org.checkpoint.dominio.diario.Conquista;
import org.checkpoint.dominio.diario.ConquistaId;
import org.checkpoint.dominio.diario.Diario;
import org.checkpoint.dominio.diario.DiarioId;
import org.checkpoint.dominio.diario.RegistroDiario;
import org.checkpoint.dominio.diario.RegistroId;
import org.checkpoint.dominio.email.Token;
import org.checkpoint.dominio.email.VerificacaoEmail;
import org.checkpoint.dominio.jogo.*;
import org.checkpoint.dominio.lista.ListaId;
import org.checkpoint.dominio.lista.ListaJogos;
import org.checkpoint.dominio.user.RedeSocial;
import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.user.UserId;
import org.checkpoint.persistencia.jpa.comentario.ComentarioJpa;
import org.checkpoint.persistencia.jpa.diario.ConquistaJpa;
import org.checkpoint.persistencia.jpa.diario.DiarioJpa;
import org.checkpoint.persistencia.jpa.diario.RegistroJpa;
import org.checkpoint.persistencia.jpa.email.VerificacaoEmailJpa;
import org.checkpoint.persistencia.jpa.jogo.AvaliacaoJpa;
import org.checkpoint.persistencia.jpa.jogo.JogoJpa;
import org.checkpoint.persistencia.jpa.jogo.RequisitosDeSistemaJpa;
import org.checkpoint.persistencia.jpa.jogo.TagJpa;
import org.checkpoint.persistencia.jpa.lista.ListaJpa;
import org.checkpoint.persistencia.jpa.user.RedeSocialJpa;
import org.checkpoint.persistencia.jpa.user.UserJpa;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Component;

@Component
public class JpaMapeador extends ModelMapper {

    public JpaMapeador() {
        var config = getConfiguration();
        config.setFieldMatchingEnabled(true);
        config.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        // Usuario
        addConverter(new AbstractConverter<UserJpa, User>() {
            @Override
            protected User convert(UserJpa source) {
                try {
                    if (source == null)
                        return null;

                    var id = new UserId(source.id);

                    DiarioId diarioId = null;
                    if (source.diario != null) {
                        diarioId = new DiarioId(source.diario.id);
                    }

                    List<RedeSocial> redesSociais = new ArrayList<>();

                    if (source.redesSociais != null) {
                        for (RedeSocialJpa redeSocial : source.redesSociais) {
                            redesSociais.add(map(redeSocial, RedeSocial.class));
                        }
                    }

                    List<UserId> solicitacoesPendentes = new ArrayList<>();

                    if (source.solicitacoesPendentes != null) {
                        for (Integer userId : source.solicitacoesPendentes) {
                            solicitacoesPendentes.add(map(userId, UserId.class));
                        }
                    }

                    List<UserId> seguindo = new ArrayList<>();

                    if (source.seguindo != null) {
                        for (Integer userId : source.seguindo) {
                            seguindo.add(map(userId, UserId.class));
                        }
                    }

                    List<UserId> seguidores = new ArrayList<>();

                    if (source.seguidores != null) {
                        for (Integer userId : source.seguidores) {
                            seguidores.add(map(userId, UserId.class));
                        }
                    }

                    List<JogoId> jogos = new ArrayList<>();

                    if (source.jogosFavoritos != null) {
                        for (Integer jogoId : source.jogosFavoritos) {
                            jogos.add(map(jogoId, JogoId.class));
                        }
                    }

                    List<ListaId> listas = new ArrayList<>();

                    if (source.listas != null) {
                        for (ListaJpa lista : source.listas) {
                            listas.add(map(lista, ListaId.class));
                        }
                    }

                    return new User(id, source.nome, source.email, source.senha, source.avatarUrl, source.bio, source.isPrivate,
                            source.emailVerificado, diarioId, solicitacoesPendentes, redesSociais, seguindo, seguidores,
                            listas,
                            jogos);
                } catch (Exception e) {
                    System.out.println("Erro ao mapear UserJpa para User: " + e.getMessage());
                    return null;
                }
            }
        });
        addConverter(new AbstractConverter<UserJpa, UserId>() {
            @Override
            protected UserId convert(UserJpa source) {
                return map(source.id, UserId.class);
            }
        });
        addConverter(new AbstractConverter<Integer, UserId>() {
            @Override
            protected UserId convert(Integer source) {
                return new UserId(source);
            }
        });

        // Rede Social
        addConverter(new AbstractConverter<RedeSocialJpa, RedeSocial>() {
            @Override
            protected RedeSocial convert(RedeSocialJpa source) {
                return new RedeSocial(source.plataforma, source.username);
            }
        });

        // Diario
        addConverter(new AbstractConverter<DiarioJpa, Diario>() {
            @Override
            protected Diario convert(DiarioJpa source) {
                try {
                    var id = map(source.id, DiarioId.class);
                    var userId = map(source.dono, UserId.class);

                    List<RegistroDiario> registros = new ArrayList<>();

                    if (source.registros != null) {
                        for (RegistroJpa registro : source.registros) {
                            registros.add(map(registro, RegistroDiario.class));
                        }
                    }

                    return new Diario(id, userId, registros);
                } catch (Exception e) {
                    System.out.println("Erro ao mapear DiarioJpa para Diario: " + e.getMessage());
                    return null;
                }
            }
        });
        addConverter(new AbstractConverter<DiarioJpa, DiarioId>() {
            @Override
            protected DiarioId convert(DiarioJpa source) {
                return map(source.id, DiarioId.class);
            }
        });
        addConverter(new AbstractConverter<Integer, DiarioId>() {
            @Override
            protected DiarioId convert(Integer source) {
                return new DiarioId(source);
            }
        });

        // Registro Diario
        addConverter(new AbstractConverter<RegistroJpa, RegistroDiario>() {
            @Override
            protected RegistroDiario convert(RegistroJpa source) {
                try {
                    var registroId = map(source.id, RegistroId.class);

                    List<Conquista> conquistas = new ArrayList<>();

                    if (source.conquistas != null) {
                        for (ConquistaJpa conquista : source.conquistas) {
                            conquistas.add(map(conquista, Conquista.class));
                        }
                    }

                    return new RegistroDiario(
                            registroId,
                            map(source.jogoId, JogoId.class),
                            null,
                            source.dataInicio,
                            source.dataTermino,
                            conquistas
                    );
                } catch (Exception e) {
                    System.out.println("Erro ao mapear RegistroJpa para RegistroDiario: " + e.getMessage());
                    return null;
                }
            }
        });
        addConverter(new AbstractConverter<RegistroJpa, RegistroId>() {
            @Override
            protected RegistroId convert(RegistroJpa source) {
                return map(source.id, RegistroId.class);
            }
        });
        addConverter(new AbstractConverter<Integer, RegistroId>() {
            @Override
            protected RegistroId convert(Integer source) {
                return new RegistroId(source);
            }
        });


        // Conquista Diario
        addConverter(new AbstractConverter<ConquistaJpa, Conquista>() {
            @Override
            protected Conquista convert(ConquistaJpa source) {
                try {
                    var conquistaId = map(source.id, ConquistaId.class);
                    return new Conquista(conquistaId, source.nome, source.dataDesbloqueada, source.concluida, null);
                } catch (Exception e) {
                    System.out.println("Erro ao mapear ConquistaJpa para Conquista: " + e.getMessage());
                    return null;
                }
            }
        });
        addConverter(new AbstractConverter<ConquistaJpa, ConquistaId>() {
            @Override
            protected ConquistaId convert(ConquistaJpa source) {
                return map(source.id, ConquistaId.class);
            }
        });
        addConverter(new AbstractConverter<Integer, ConquistaId>() {
            @Override
            protected ConquistaId convert(Integer source) {
                return new ConquistaId(source);
            }
        });

        // Verificacao Email
        addConverter(new AbstractConverter<VerificacaoEmailJpa, VerificacaoEmail>() {
            @Override
            protected VerificacaoEmail convert(VerificacaoEmailJpa source) {
                try {
                    var userId = map(source.userId, UserId.class);
                    return new VerificacaoEmail(map(source.token, Token.class), userId, source.dataExpiracao);
                } catch (Exception e) {
                    System.out.println("Erro ao mapear VerificacaoEmailJpa para VerificacaoEmail: " + e.getMessage());
                    return null;
                }
            }
        });
        addConverter(new AbstractConverter<VerificacaoEmailJpa, Token>() {
            @Override
            protected Token convert(VerificacaoEmailJpa source) {
                return map(source.token, Token.class);
            }
        });
        addConverter(new AbstractConverter<String, Token>() {
            @Override
            protected Token convert(String source) {
                return new Token(source);
            }
        });

        // Lista
        addConverter(new AbstractConverter<ListaJpa, ListaJogos>() {
            @Override
            protected ListaJogos convert(ListaJpa source) {
                ListaId listaId = new ListaId(source.id);

                List<UserId> curtidas = new ArrayList<>();

                if (source.curtidas != null) {
                    for (Integer curtida : source.curtidas) {
                        curtidas.add(map(curtida, UserId.class));
                    }
                }

                List<JogoId> jogos = new ArrayList<>();

                if (source.jogos != null) {
                    for (Integer jogoId : source.jogos) {
                        jogos.add(map(jogoId, JogoId.class));
                    }
                }

                return new ListaJogos(
                        listaId,
                        map(source.dono, UserId.class),
                        source.titulo,
                        source.isPrivate,
                        jogos,
                        curtidas
                );
            }
        });
        addConverter(new AbstractConverter<ListaJpa, ListaId>() {
            @Override
            protected ListaId convert(ListaJpa source) {
                return map(source.id, ListaId.class);
            }
        });
        addConverter(new AbstractConverter<Integer, ListaId>() {
            @Override
            protected ListaId convert(Integer source) {
                return new ListaId(source);
            }
        });

        // Tag
        addConverter(new AbstractConverter<TagJpa, Tag>() {
            @Override
            protected Tag convert(TagJpa source) {
                TagId tagId = new TagId(source.id);

                List<UserId> votos = new ArrayList<>();

                if (source.votos != null) {
                    for (Integer voto : source.votos) {
                        votos.add(map(voto, UserId.class));
                    }
                }

                return new Tag(
                        tagId,
                        source.nome,
                        votos,
                        null
                );
            }
        });
        addConverter(new AbstractConverter<TagJpa, TagId>() {
            @Override
            protected TagId convert(TagJpa source) {
                return map(source.id, TagId.class);
            }
        });
        addConverter(new AbstractConverter<Integer, TagId>() {
            @Override
            protected TagId convert(Integer source) {
                return new TagId(source);
            }
        });

        // Requisitos de Sistema
        addConverter(new AbstractConverter<RequisitosDeSistemaJpa, RequisitosDeSistema>() {
            @Override
            protected RequisitosDeSistema convert(RequisitosDeSistemaJpa source) {
                RequisitosDeSistemaId requisitosId = new RequisitosDeSistemaId(source.id);

                return new RequisitosDeSistema(
                        requisitosId,
                        map(source.jogo, JogoId.class),
                        source.sistemaOp,
                        source.processador,
                        source.memoria,
                        source.placaVideo,
                        source.tipo
                );
            }
        });
        addConverter(new AbstractConverter<RequisitosDeSistemaJpa, RequisitosDeSistemaId>() {
            @Override
            protected RequisitosDeSistemaId convert(RequisitosDeSistemaJpa source) {
                return map(source.id, RequisitosDeSistemaId.class);
            }
        });
        addConverter(new AbstractConverter<Integer, RequisitosDeSistemaId>() {
            @Override
            protected RequisitosDeSistemaId convert(Integer source) {
                return new RequisitosDeSistemaId(source);
            }
        });

        // Avaliacao
        addConverter(new AbstractConverter<AvaliacaoJpa, Avaliacao>() {
            @Override
            protected Avaliacao convert(AvaliacaoJpa source) {
                AvaliacaoId avaliacaoId = new AvaliacaoId(source.id);

                List<UserId> curtidas = new ArrayList<>();

                if (source.curtidas != null) {
                    for (Integer curtida : source.curtidas) {
                        curtidas.add(map(curtida, UserId.class));
                    }
                }

                return new Avaliacao(
                        avaliacaoId,
                        map(source.autorId, UserId.class),
                        map(source.jogo, JogoId.class),
                        source.nota,
                        source.comentario,
                        source.data,
                        curtidas
                );
            }
        });
        addConverter(new AbstractConverter<RequisitosDeSistemaJpa, AvaliacaoId>() {
            @Override
            protected AvaliacaoId convert(RequisitosDeSistemaJpa source) {
                return map(source.id, AvaliacaoId.class);
            }
        });
        addConverter(new AbstractConverter<Integer, AvaliacaoId>() {
            @Override
            protected AvaliacaoId convert(Integer source) {
                return new AvaliacaoId(source);
            }
        });

        // Jogo
        addConverter(new AbstractConverter<JogoJpa, Jogo>() {
            @Override
            protected Jogo convert(JogoJpa source) {
                JogoId jogoId = new JogoId(source.id);

                List<UserId> curtidas = new ArrayList<>();

                if (source.curtidas != null) {
                    for (Integer curtida : source.curtidas) {
                        curtidas.add(map(curtida, UserId.class));
                    }
                }

                List<Tag> tags = new ArrayList<>();

                if (source.tags != null) {
                    for (TagJpa tag : source.tags) {
                        tags.add(map(tag, Tag.class));
                    }
                }

                List<RequisitosDeSistema> requisitos = new ArrayList<>();

                if (source.requisitos != null) {
                    for (RequisitosDeSistemaJpa requisito : source.requisitos) {
                        requisitos.add(map(requisito, RequisitosDeSistema.class));
                    }
                }

                List<Avaliacao> avaliacoes = new ArrayList<>();

                if (source.avaliacoes != null) {
                    for (AvaliacaoJpa avaliacao : source.avaliacoes) {
                        avaliacoes.add(map(avaliacao, Avaliacao.class));
                    }
                }

                return new Jogo(
                        jogoId,
                        source.nome,
                        source.company,
                        source.capaUrl,
                        source.informacaoTitulo,
                        source.informacaoDescricao,
                        source.nota,
                        source.capturas.stream().toList(),
                        curtidas,
                        tags,
                        requisitos,
                        avaliacoes
                );
            }
        });
        addConverter(new AbstractConverter<JogoJpa, JogoId>() {
            @Override
            protected JogoId convert(JogoJpa source) {
                return map(source.id, JogoId.class);
            }
        });
        addConverter(new AbstractConverter<Integer, JogoId>() {
            @Override
            protected JogoId convert(Integer source) {
                return new JogoId(source);
            }
        });

        // Comentario
        addConverter(new AbstractConverter<ComentarioJpa, Comentario>() {
            @Override
            protected Comentario convert(ComentarioJpa source) {
                ComentarioId comentarioId = new ComentarioId(source.id);

                List<UserId> curtidas = new ArrayList<>();

                if (source.curtidas != null) {
                    for (Integer curtida : source.curtidas) {
                        curtidas.add(map(curtida, UserId.class));
                    }
                }

                AvaliacaoId avaliacaoId = null;
                ListaId listaId = null;
                ComentarioId comentarioPaiId = null;

                if(source.avaliacaoAlvoId != null) {
                    avaliacaoId = map(source.avaliacaoAlvoId, AvaliacaoId.class);
                }

                if(source.listaAlvoId != null) {
                    listaId = map(source.listaAlvoId, ListaId.class);
                }

                if(source.comentarioPaiId != null) {
                    comentarioPaiId = map(source.comentarioPaiId, ComentarioId.class);
                }

                return new Comentario(
                        comentarioId,
                        map(source.autorId, UserId.class),
                        source.conteudo,
                        source.data,
                        avaliacaoId,
                        listaId,
                        comentarioPaiId,
                        curtidas
                );
            }
        });
        addConverter(new AbstractConverter<ComentarioJpa, ComentarioId>() {
            @Override
            protected ComentarioId convert(ComentarioJpa source) {
                return map(source.id, ComentarioId.class);
            }
        });
        addConverter(new AbstractConverter<Integer, ComentarioId>() {
            @Override
            protected ComentarioId convert(Integer source) {
                return new ComentarioId(source);
            }
        });
    }
}
