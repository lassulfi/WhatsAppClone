package whatsappclone.cursoandroid.com.whatsappclone.model;

/**
 * Created by LuisDaniel on 01/08/2017.
 */

public class    Contato {

    //Atributos
    private String identificadorUsuario;
    private String nome;
    private String email;

    //Construtor
    public Contato(){
    }

    //Métodos Getters & Setter
    public String getIdentificadorUsuario() {
        return identificadorUsuario;
    }

    public void setIdentificadorUsuario(String identificadorUsuario) {
        this.identificadorUsuario = identificadorUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
