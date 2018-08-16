package whatsappclone.cursoandroid.com.whatsappclone.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsappclone.cursoandroid.com.whatsappclone.R;
import whatsappclone.cursoandroid.com.whatsappclone.adapter.MensagemAdaper;
import whatsappclone.cursoandroid.com.whatsappclone.config.FirebaseConfig;
import whatsappclone.cursoandroid.com.whatsappclone.helper.Base64Custom;
import whatsappclone.cursoandroid.com.whatsappclone.helper.Preferences;
import whatsappclone.cursoandroid.com.whatsappclone.model.Conversa;
import whatsappclone.cursoandroid.com.whatsappclone.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    //Atributos
    private Toolbar toolbar;

    //Dados do destinatario da conversa
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;

    //Dados do rementente
    private String idUsuarioRemetente;
    private String nomeUsuarioRemetente;

    private EditText mensagemEditText;
    private ImageButton enviarImageButton;

    //Instancia do Firebase
    private DatabaseReference databaseReference;

    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        //Recuperando os objetos da tela
        toolbar = (Toolbar) findViewById(R.id.tb_conversa);
        mensagemEditText = (EditText) findViewById(R.id.mensagem_edit_text);
        enviarImageButton = (ImageButton) findViewById(R.id.enviar_image_button);
        listView = (ListView) findViewById(R.id.lv_conversas);

        //Recupernado os dados do usuario logado
        Preferences preferences = new Preferences(ConversaActivity.this);
        idUsuarioRemetente = preferences.getIdentificador();
        nomeUsuarioRemetente = preferences.getUsername();

        //Recuperando os dados passados do ContatosFragment
        Bundle extra = getIntent().getExtras();

        //Verificando se os dados realmente existem
        if (extra != null){
            nomeUsuarioDestinatario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            idUsuarioDestinatario = Base64Custom.encodeBase64(emailDestinatario);
        }

        //Configuracao da Toolbar
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Montagem da listView e Adapter
        mensagens = new ArrayList<>();
        /*adapter = new ArrayAdapter(ConversaActivity.this,
                android.R.layout.simple_list_item_1,
                mensagens);*/
        adapter = new MensagemAdaper(ConversaActivity.this,mensagens);
        listView.setAdapter(adapter);

        //Recuperando as mensagens do Firebase
        databaseReference = FirebaseConfig.getFirebase()
                .child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        //Criar listener para mensagens
        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpando os dados que já estavam armazenados
                mensagens.clear();

                //Recuperando as mensagens no Firebase
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                //Notificando o adapter
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //Adicionado o listener ao dataBaseReference
        databaseReference.addListenerForSingleValueEvent(valueEventListenerMensagem);


        //Enviar mensagem
        enviarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Recuperando a mensagem digitada pelo usuario
                String textoMensagem = mensagemEditText.getText().toString();

                //Verificando se o texto está preenchido
                if (textoMensagem.isEmpty()){
                    Toast.makeText(ConversaActivity.this,"Digite uma mensagem",Toast.LENGTH_SHORT).show();
                } else {
                    //Criando o objeto mensagem
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(textoMensagem);

                    //Chamada do método para salvar mensagem no Firebase
                    //Salvando a mensagem do remetente para o destinatario
                    Boolean retornoMensagemRemetente =
                            salvarMensagem(idUsuarioRemetente,idUsuarioDestinatario,mensagem);
                    if (!retornoMensagemRemetente){
                        Toast.makeText(ConversaActivity.this,
                                "Erro ao salvar mensagem. Tente novamente.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        //Salvando a mensagem do destinatario para o remetente
                        Boolean retornoMensagemDestinatario =
                                salvarMensagem(idUsuarioDestinatario,idUsuarioRemetente,mensagem);
                        if (!retornoMensagemDestinatario){
                            Toast.makeText(ConversaActivity.this,
                                    "Problema ao enviar a mensagem. Tente novamente.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    //Salvar a conversa para o remetente
                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(idUsuarioDestinatario);
                    conversa.setNome(nomeUsuarioDestinatario);
                    conversa.setMensagem(textoMensagem);
                    Boolean retornoConversaRemetente =
                            salvarConversa(idUsuarioRemetente,idUsuarioDestinatario,conversa);
                    if (!retornoConversaRemetente){
                        Toast.makeText(ConversaActivity.this,
                                "Erro ao salvar conversa. Tente novamente.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        conversa.setIdUsuario(idUsuarioRemetente);
                        conversa.setNome(nomeUsuarioRemetente);
                        conversa.setMensagem(textoMensagem);
                        //Salvar a conversa para o destinatario
                        Boolean retornoConversaDestinatario =
                                salvarConversa(idUsuarioDestinatario,idUsuarioRemetente,conversa);
                        if (!retornoConversaDestinatario){
                            Toast.makeText(ConversaActivity.this,
                                    "Erro ao salvar conversa para o destinatario. Tente novamente.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                    mensagemEditText.setText("");

                }

            }
        });

    }

    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){

        try{
            //Recuperando a instancia do objeto para manipular o banco de dados
            databaseReference = FirebaseConfig.getFirebase().child("mensagens");
            //Criacao da estrutura de nos com o id do remetente
            databaseReference.child(idRemetente)
                    .child(idDestinatario)
                    .push()
                    .setValue(mensagem);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    private boolean salvarConversa(String idRemetente, String idDestinatario, Conversa conversa){
        try {
            //Recuperando a instancia do objeto para manipular o banco de dados
            databaseReference = FirebaseConfig.getFirebase().child("conversas");
            //Criacao da estrutura de nos com o id do remetente
            databaseReference
                    .child(idRemetente)
                    .child(idDestinatario)
                    .setValue(conversa);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Excluindo o listener
        databaseReference.removeEventListener(valueEventListenerMensagem);
    }
}
