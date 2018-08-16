package whatsappclone.cursoandroid.com.whatsappclone.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsappclone.cursoandroid.com.whatsappclone.R;
import whatsappclone.cursoandroid.com.whatsappclone.activity.ConversaActivity;
import whatsappclone.cursoandroid.com.whatsappclone.adapter.ConversaAdapter;
import whatsappclone.cursoandroid.com.whatsappclone.config.FirebaseConfig;
import whatsappclone.cursoandroid.com.whatsappclone.helper.Base64Custom;
import whatsappclone.cursoandroid.com.whatsappclone.helper.Preferences;
import whatsappclone.cursoandroid.com.whatsappclone.model.Conversa;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    //Atributos
    private ListView listView;
    private ArrayAdapter<Conversa> adapter;
    private ArrayList<Conversa> conversas;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerConversas;


    public ConversasFragment() {
        // Required empty public constructor
    }

    /**
     * Método onStart cria o evento ValueEventListener um pouco antes da criacao do Fragment
     */

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerConversas);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerConversas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        //Instancia dos objetos
        conversas = new ArrayList<>();

        //Monta a listview e adapter
        listView = (ListView) view.findViewById(R.id.conversas_listview);
        adapter = new ConversaAdapter(getActivity(),conversas);
        listView.setAdapter(adapter);

        //Recuperando a instancia do Firebase onde estão salvas as ultimas conversas
        //Recuperando o usuario que está logado para definir o nó do Firebase
        Preferences preferences = new Preferences(getActivity());
        String idUsuarioLogado = preferences.getIdentificador();
        databaseReference = FirebaseConfig.getFirebase().
                child("conversas").child(idUsuarioLogado);

        //Listener para recuperar os dados
        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpando a list
                conversas.clear();

                //Recuperando as ultimas conversas
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    //Recuperando os dados do banco de dados e armazenando na classe Conversa
                    Conversa conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);
                }

                //Informando o adapter que a lista de conversas foi atualizada
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //Adicionando um evento de click na lista para exibir a ConversaActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Criando a intent da ConversaActivity
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                //Recupera os dados a serem passados
                Conversa conversa = conversas.get(position);

                //Enviando dados para conversa Activity
                intent.putExtra("nome",conversa.getNome());
                //Convertendo a id do usuario para email
                String email = Base64Custom.decodeBase64(conversa.getIdUsuario());
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });

        return  view;
    }

}
