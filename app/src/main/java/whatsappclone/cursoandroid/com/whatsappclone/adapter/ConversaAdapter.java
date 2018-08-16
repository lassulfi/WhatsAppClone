package whatsappclone.cursoandroid.com.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import whatsappclone.cursoandroid.com.whatsappclone.R;
import whatsappclone.cursoandroid.com.whatsappclone.model.Conversa;


/**
 * Created by LuisDaniel on 08/08/2017.
 */

public class ConversaAdapter extends ArrayAdapter<Conversa> {

    //Atributos
    private ArrayList<Conversa> conversas;
    private Context context;


    //Construtor
    public ConversaAdapter(Context c, ArrayList<Conversa> objects) {
        super(c, 0, objects);
        this.conversas = objects;
        this.context = c;
    }

    //Métodos

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Criando a view
        View view = null;

        //Verifica se a lista de conversas está vazia
        if (conversas != null){
            //Inicializa o objeto para a montagem da view
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Montagem da view a partir do xml
            view = inflater.inflate(R.layout.lista_conversas,parent,false);

            //Recupera o elemento para exibicao
            TextView nomeContato = (TextView) view.findViewById(R.id.conversa_nome_textview);
            TextView ultimaConversa = (TextView) view.findViewById(R.id.conversa_textview);

            //Passando os dados da arraylist para a classe conversa
            Conversa conversa = conversas.get(position);

            //Exibiindo o nome e a ultima conversa
            nomeContato.setText(conversa.getNome());
            ultimaConversa.setText(conversa.getMensagem());

        }

        return view;
    }
}
