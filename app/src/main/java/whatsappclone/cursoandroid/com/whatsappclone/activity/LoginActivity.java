package whatsappclone.cursoandroid.com.whatsappclone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

import whatsappclone.cursoandroid.com.whatsappclone.Manifest;
import whatsappclone.cursoandroid.com.whatsappclone.R;
import whatsappclone.cursoandroid.com.whatsappclone.config.FirebaseConfig;
import whatsappclone.cursoandroid.com.whatsappclone.helper.Base64Custom;
import whatsappclone.cursoandroid.com.whatsappclone.helper.Preferences;
import whatsappclone.cursoandroid.com.whatsappclone.helper.WhatsAppPermissions;
import whatsappclone.cursoandroid.com.whatsappclone.model.Usuario;

import static android.os.Build.VERSION_CODES.M;

public class LoginActivity extends AppCompatActivity {

    //Atributos da classe
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private EditText emailEditText;
    private EditText senhaEditText;
    private Button logarButton;

    private Usuario usuario;

    private ValueEventListener valueEventListenerUsuario;
    private String identificadorUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Chamada do método para verificar se o usuario já está logado
        verificarUsuarioLogado();

        emailEditText = (EditText) findViewById(R.id.edit_text_email);
        senhaEditText = (EditText) findViewById(R.id.edit_text_senha);
        logarButton = (Button) findViewById(R.id.button_logar);

        logarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Instanciando o usuario
                usuario = new Usuario();
                //Recuperando o email e senha do usuario para realizacao do login
                usuario.setEmail(emailEditText.getText().toString());
                usuario.setSenha(senhaEditText.getText().toString());

                //Validacao do login
                validarLogin();
            }
        });

    }

    /**
     * Método para verificar se o usuario já está logado.
     * Se o usario já estiver logado chama a MainActivity
     */
    private void verificarUsuarioLogado(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        if (firebaseAuth.getCurrentUser() != null){
            abrirTelaPrincipal();
        }

    }

    /**
     * Método para validar e realizar o login do usuario no Firebase
     */
    private void validarLogin(){

        //Instanciando o metodo de autenticacao do Firebase
        firebaseAuth = FirebaseConfig.getFirebaseAuth();

        //Chamada do método para realizar o login no Firebase
        firebaseAuth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Veriificando se o processo de login e autenticacao foi bem sucedido
                if (task.isSuccessful()){

                    identificadorUsuarioLogado = Base64Custom.encodeBase64(usuario.getEmail());

                    //Recuperando a instancia do Firebase
                    databaseReference = FirebaseConfig.getFirebase()
                            .child("usuario")
                            .child(identificadorUsuarioLogado);

                    //Fazendo a consulta no Firebase
                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            /*Salvando o nome do usuario no SharedPreferences ao fazer o login
                            Dessa maneira não é necessário fazer uma consulta toda vez que for
                            necessário recuperar o nome do usuario no app*/

                            Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);

                            //Salvando o email usuario na preferencias ao fazer o login
                            Preferences preferences = new Preferences(LoginActivity.this);
                            preferences.saveUserPreferences(identificadorUsuarioLogado,
                                    usuarioRecuperado.getNome());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    databaseReference.addListenerForSingleValueEvent(valueEventListenerUsuario);

                    //Se o login ocorreu de maneira correta o usuario vai para a tela principal
                    // e fecha essa activity
                    abrirTelaPrincipal();
                    /*
                    Toast.makeText(LoginActivity.this,"Login efetuado com sucesso!",
                            Toast.LENGTH_SHORT).show();
                    */
                } else {
                    Toast.makeText(LoginActivity.this,"Não foi possível realizar o login.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Método para abrir a tela principal
     */
    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Método para abrir a tela de cadastro de usuario
     * @param view
     */
    public void abrirCadastroUsuario(View view){

        Intent intent = new Intent(LoginActivity.this,CadastroUsuarioActivity.class);

        startActivity(intent);

    }

}
