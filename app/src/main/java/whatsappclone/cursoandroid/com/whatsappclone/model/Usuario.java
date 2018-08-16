package whatsappclone.cursoandroid.com.whatsappclone.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import whatsappclone.cursoandroid.com.whatsappclone.config.FirebaseConfig;

/**
 * Classe de modelo de usuario
 * Created by LuisDaniel on 28/07/2017.
 */

public class Usuario {

    //Atributos
    private String Id; //Id do usuario
    private String nome; //username
    private String email; //email
    private String senha; //senha

    //Construtor
    public Usuario() {
    }

    //Métodos

    /**
     * Método para salvar o usuario no banco de dados
     */
    public void salvar(){
        //Instanciando a referencia ao banco de dados do firebase
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        //Criando os nós no Firebase e salvando os usuarios
        databaseReference.child("usuario").child(getId()).setValue(this);
    }

    //Getters & Setters
    @Exclude
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
