package whatsappclone.cursoandroid.com.whatsappclone.model;

/**
 * Classe para armazenar mensagem no Firebase
 *
 * Created by LuisDaniel on 03/08/2017.
 */

public class Mensagem {

    //Atributos
    private String idUsuario;
    private String mensagem;

    //Construtor
    public Mensagem() {
    }

    //Métodos especiais
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
