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
import whatsappclone.cursoandroid.com.whatsappclone.adapter.ContatoAdapter;
import whatsappclone.cursoandroid.com.whatsappclone.config.FirebaseConfig;
import whatsappclone.cursoandroid.com.whatsappclone.helper.Preferences;
import whatsappclone.cursoandroid.com.whatsappclone.model.Contato;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    //Atributos
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerContatos;


    public ContatosFragment() {
        // Required empty public constructor
    }

    /**
     * Método onStart cria o evento ValueEventListener um pouco antes da criacao do Fragment
     */
    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerContatos);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerContatos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Instancia dos objetos
        contatos = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        /*
        contatos.add("Dummy data 1");
        contatos.add("Dummy data 2");
        contatos.add("Dummy data 3");
        */

        //Monta a listview e adapter
        listView = (ListView) view.findViewById(R.id.contatos_listview);
        /*
        adapter = new ArrayAdapter(getActivity(),R.layout.lista_contatos,
                contatos);
        */
        adapter = new ContatoAdapter(getActivity(),contatos);

        listView.setAdapter(adapter);

        //Recuperando a instancia do Firebase
        //Recuperando o usuario que está logado para definir o nó do Firebase
        Preferences preferences = new Preferences(getActivity());
        String idUsuarioLogado = preferences.getIdentificador();
        databaseReference = FirebaseConfig.getFirebase().
                child("contatos").child(idUsuarioLogado);

        //Listener para recuperar os dados

        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpando a list
                contatos.clear();

                //Recuperando os contatos
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    //Recuperando os dados do banco de dados e armazenando na classe Contato
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato);
                }
                //Informando o adapter que a lista de contatos foi atualizada
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //Adicionando um evento de click na lista para exibir a ConversaActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Criando a intent da ConversaActivity
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                //Recupera os dados a serem passados
                Contato contato = contatos.get(position);

                //Enviando dados para conversa Activity
                intent.putExtra("nome",contato.getNome());
                intent.putExtra("email",contato.getEmail());
                startActivity(intent);
            }
        });

        return  view;
    }

}
