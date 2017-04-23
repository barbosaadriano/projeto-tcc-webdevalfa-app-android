package br.com.avantagem.appavws;

/**
 * Created by Administrador on 21/04/2017.
 */

public class EventosListaModelo {

    private String descricao;
    private String momento;
    private String dados;
    private String dispositivo;
    private String local;

    public EventosListaModelo(String descricao, String momento, String dados, String dispositivo, String local) {
        this.descricao = descricao;
        this.momento = momento;
        this.dados = dados;
        this.dispositivo = dispositivo;
        this.local = local;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMomento() {
        return momento;
    }

    public void setMomento(String momento) {
        this.momento = momento;
    }

    public String getDados() {
        return dados;
    }

    public void setDados(String dados) {
        this.dados = dados;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
