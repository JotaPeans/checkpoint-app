package org.checkpoint.dominio.jogo;

import static org.apache.commons.lang3.Validate.notNull;

public class RequisitosDeSistema {
    private final RequisitosDeSistemaId id;

    private final JogoId jogoId;

    private String sistemaOp;
    private String processador;
    private String memoria;
    private String placaVideo;
    private String tipo;

    public RequisitosDeSistema(
            RequisitosDeSistemaId id,
            JogoId jogoId,
            String sistemaOp,
            String processador,
            String memoria,
            String placaVideo,
            String tipo
    ) {
        this.jogoId = jogoId;
        notNull(id, "O id não pode ser nulo");
        notNull(sistemaOp, "O sistema operacional não pode ser nulo");
        notNull(processador, "O processador não pode ser nulo");
        notNull(memoria, "A memoria não pode ser nula");
        notNull(placaVideo, "A placa de vídeo não pode ser nula");
        notNull(tipo, "O tipo do requisitos de sistema não pode ser nula");

        this.id = id;

        setSistemaOp(sistemaOp);
        setProcessador(processador);
        setMemoria(memoria);
        setPlacaVideo(placaVideo);
        setTipo(tipo);
    }

    public RequisitosDeSistemaId getId() {
        return id;
    }

    public JogoId getJogoId() {
        return jogoId;
    }

    public String getSistemaOp() {
        return sistemaOp;
    }

    public void setSistemaOp(String sistemaOp) {
        this.sistemaOp = sistemaOp;
    }

    public String getProcessador() {
        return processador;
    }

    public void setProcessador(String processador) {
        this.processador = processador;
    }

    public String getMemoria() {
        return memoria;
    }

    public void setMemoria(String memoria) {
        this.memoria = memoria;
    }

    public String getPlacaVideo() {
        return placaVideo;
    }

    public void setPlacaVideo(String placaVideo) {
        this.placaVideo = placaVideo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return this.sistemaOp + ", " + this.processador + ", " + this.memoria + ", " + this.placaVideo + ", " + this.tipo;
    }
}