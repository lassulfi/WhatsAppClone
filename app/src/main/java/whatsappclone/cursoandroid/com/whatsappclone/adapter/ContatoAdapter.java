package whatsappclone.cursoandroid.com.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import whatsappclone.cursoandroid.com.whatsappclone.R;
import whatsappclone.cursoandroid.com.whatsappclone.model.Contato;


/**
 * Created by LuisDaniel on 02/08/2017.
 */

public class ContatoAdapter extends ArrayAdapter<Contato>{

    //Atributos
    private ArrayList<Contato> contatos;
    private Context context;

    //Construtor
    public ContatoAdapter(Context c, ArrayList<Contato> objects) {
        super(c, 0, objects);
        this.contatos = objects;
        this.context = c;
    }

    //Métodos

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Criando a view
        View view = null;

        //Verifica se a lista de contatos está vazia
        if (contatos != null){
            //Inicializa objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Montagem da view a partir do xml
            view = inflater.inflate(R.layout.lista_contatos,parent,false);

            //Recupera o elemento para exibicao
            TextView nomeContato = (TextView) view.findViewById(R.id.nome_textview);
            TextView emailContato = (TextView) view.findViewById(R.id.email_textview);

            //Passando os dados do arraylist para a classe contato
            Contato contato = contatos.get(position);

            //Exibindo o nome do contato
            nomeContato.setText(contato.getNome());
            emailContato.setText(contato.getEmail());

        }

        return view;

    }
}
