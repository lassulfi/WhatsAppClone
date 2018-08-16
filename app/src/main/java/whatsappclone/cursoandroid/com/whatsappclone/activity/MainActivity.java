package whatsappclone.cursoandroid.com.whatsappclone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import whatsappclone.cursoandroid.com.whatsappclone.R;
import whatsappclone.cursoandroid.com.whatsappclone.adapter.TabAdapter;
import whatsappclone.cursoandroid.com.whatsappclone.config.FirebaseConfig;
import whatsappclone.cursoandroid.com.whatsappclone.helper.Base64Custom;
import whatsappclone.cursoandroid.com.whatsappclone.helper.Preferences;
import whatsappclone.cursoandroid.com.whatsappclone.helper.SlidingTabLayout;
import whatsappclone.cursoandroid.com.whatsappclone.model.Contato;
import whatsappclone.cursoandroid.com.whatsappclone.model.Usuario;

public class MainActivity extends AppCompatActivity {

    //Instanciamento dos objetos da classe

    //Teste do banco de dados
    //private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    //private Button buttonSair;
    //Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    //Tabs
    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    //Adicionar contatos
    private String identificadorContato;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instanciando a autenticacao do usuario
        firebaseAuth = FirebaseConfig.getFirebaseAuth();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Configuracao da Toolbar
        toolbar.setTitle(R.string.app_name);
        //Chamada do suporte da Toolbar para que ela funcione corretamente
        setSupportActionBar(toolbar);

        //Configurando as Tabs
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //Configuracao da slidingTabLayout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccent));

        //Configuracao do Adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        //Passando o viewPager para o slidingTabLayout
        slidingTabLayout.setViewPager(viewPager);

        //databaseReference.child("teste").setValue("100");

        /*
        buttonSair = (Button) findViewById(R.id.button_sair_app);

        buttonSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Recuperando o objeto de autenticacao do Firebase
                firebaseAuth = FirebaseConfig.getFirebaseAuth();

                //Fazendo o logoff do Firebase
                firebaseAuth.signOut();

                Toast.makeText(MainActivity.this,"Log-out realizado com sucesso!",Toast.LENGTH_SHORT).show();

                //Voltando para a tela de login
                startActivity(new Intent(MainActivity.this,LoginActivity.class));

            }
        });
     */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Criando um objeto tipo inflater para inflar o menu da MainActivity
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Para cada item selecionado será realizado um tratamento diferente
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_sair:
                //Chamada do método para fazer o logoff do usuario
                logoffUser();
                return true;
            case R.id.action_pesquisa:
                return true;
            case R.id.action_adicionar:
                abrirCadastroContato();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Método para abrir a Dialog de cadastro de contato
     */
    private void abrirCadastroContato(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //Configuracao da dialog
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("E-mail do usuário:");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        //Configuracao dos botoes
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String emailContato = editText.getText().toString();

                //Validacao se o email foi digitado
                if(emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this,"Informe o e-mail",Toast.LENGTH_SHORT).show();
                } else {
                    //Verificar se o usuario já está cadastrado no app
                    //Convertendo para base64
                    identificadorContato = Base64Custom.encodeBase64(emailContato);

                    //Recuperando a instancia firebase e o nó do Firebase onde será feita a pesquisa
                    databaseReference = FirebaseConfig.getFirebase().child("usuario").
                            child(identificadorContato);
                    //Adicionando um Listener para a consulta
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Verificando se o Listener retornou dados
                            if (dataSnapshot.getValue() != null){
                                //Recuperando os dados do contato a ser adicionado
                                Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);

                                //Recuperando o email do usuario logado do SharedPreferences
                                Preferences preferences = new Preferences(MainActivity.this);
                                String identificadorUsuarioLogado = preferences.getIdentificador();

                                //Criando um nó contatos para o usuario logado no app e passando
                                // os dados do contato a ser cadastrado
                                //Pegando uma nova referencia do Firebase
                                databaseReference = FirebaseConfig.getFirebase();
                                databaseReference = databaseReference.child("contatos")
                                        .child(identificadorUsuarioLogado)
                                        .child(identificadorContato);

                                //Instanciando a classe contato
                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario(identificadorContato);
                                contato.setEmail(usuarioContato.getEmail());
                                contato.setNome(usuarioContato.getNome());

                                databaseReference.setValue(contato);
                            } else {
                                Toast.makeText(MainActivity.this,"Seu amigo não possui cadastro!",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }

    /**
     * Método para realizacao do logoff do usuario
     */
    private void logoffUser(){
        //Deslogando o usuario
        firebaseAuth.signOut();
        //Exibe a tela de login
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }

}
