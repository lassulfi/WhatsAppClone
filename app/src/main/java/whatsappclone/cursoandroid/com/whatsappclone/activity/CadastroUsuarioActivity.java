package whatsappclone.cursoandroid.com.whatsappclone.activity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import whatsappclone.cursoandroid.com.whatsappclone.R;
import whatsappclone.cursoandroid.com.whatsappclone.config.FirebaseConfig;
import whatsappclone.cursoandroid.com.whatsappclone.helper.Base64Custom;
import whatsappclone.cursoandroid.com.whatsappclone.helper.Preferences;
import whatsappclone.cursoandroid.com.whatsappclone.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    //Atributos da classe
    private EditText nomeEditText;
    private EditText emailEditText;
    private EditText senhaEditText;
    private Button cadastrarButton;
    private Usuario usuario;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        //Recuperando os itens da tela
        nomeEditText = (EditText) findViewById(R.id.edit_text_cadastro_nome);
        emailEditText = (EditText) findViewById(R.id.edit_text_cadastro_email);
        senhaEditText = (EditText) findViewById(R.id.edit_text_cadastro_senha);
        cadastrarButton = (Button) findViewById(R.id.button_cadastrar);

        cadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Instanciando o objeto usuario
                usuario = new Usuario();

                //Passando os atributos para a classe usuario
                usuario.setNome(nomeEditText.getText().toString());
                usuario.setEmail(emailEditText.getText().toString());
                usuario.setSenha(senhaEditText.getText().toString());

                //Chamada do método para cadastrar usuario
                cadastrarUsuario();

            }
        });

    }

    private void cadastrarUsuario(){

        //Recuperando a instancia de autenticacao do usuario no Firebase
        auth = FirebaseConfig.getFirebaseAuth();

        //Cadastro do usuario no Firebase
        auth.createUserWithEmailAndPassword(usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this,
                        new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Teste do cadastro do usuario
                        if(task.isSuccessful()){
                            Toast.makeText(CadastroUsuarioActivity.this,
                                    "Cadastro efetuado com sucesso!", Toast.LENGTH_LONG).show();
                            //Definindo a ID do usuario criado pelo firebase
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            //usuario.setId(firebaseUser.getUid());
                            String userIdentifier = Base64Custom.encodeBase64(usuario.getEmail());
                            usuario.setId(userIdentifier);
                            usuario.salvar();

                            //Salvando o email usuario na preferencias ao fazer o login
                            Preferences preferences = new Preferences(CadastroUsuarioActivity.this);
                            preferences.saveUserPreferences(userIdentifier,usuario.getNome());

                            //Método que encaminha o usuario para o login
                            openUserLogin();

                            //Fazendo o logoff intencional do usuario
                            //auth.signOut();
                            //Encerrando a Activity
                            finish();
                        } else {
                            //Tratamento das exceções de cadastro de usuario
                            String erroExcecao = "";
                            try{
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erroExcecao = "Digite uma senha mais forte, contendo caracteres " +
                                        "e números.";
                                e.printStackTrace();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroExcecao = "E-mail inválido. Informe um e-mail válido.";
                                e.printStackTrace();
                            } catch (FirebaseAuthUserCollisionException e) {
                                erroExcecao = "E-mail já cadastrado.";
                                e.printStackTrace();
                            } catch (Exception e) {
                                erroExcecao = "Erro ao efetuar o cadastro";
                                e.printStackTrace();
                            }
                            Toast.makeText(CadastroUsuarioActivity.this,"Erro: " + erroExcecao,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void openUserLogin(){

        Intent intent = new Intent(CadastroUsuarioActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();

    }

}


