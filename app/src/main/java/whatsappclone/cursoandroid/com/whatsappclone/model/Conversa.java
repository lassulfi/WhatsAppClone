package whatsappclone.cursoandroid.com.whatsappclone.model;

/**
 * Created by LuisDaniel on 06/08/2017.
 */

public class Conversa {

    //Atributos
    private String idUsuario;
    private String nome;
    private String mensagem;

    //Métodos especiais
    public Conversa() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
