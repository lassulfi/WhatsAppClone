package whatsappclone.cursoandroid.com.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import whatsappclone.cursoandroid.com.whatsappclone.R;
import whatsappclone.cursoandroid.com.whatsappclone.helper.Preferences;
import whatsappclone.cursoandroid.com.whatsappclone.model.Mensagem;



/**
 * Created by LuisDaniel on 06/08/2017.
 */

public class MensagemAdaper extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdaper(Context c, ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.context = c;
        this.mensagens = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        //Verifica se a lista está preenchida
        if (mensagens != null){
            //Recupera dados do usuario remetente
            Preferences preferences = new Preferences(context);
            String idUsuarioRemetente = preferences.getIdentificador();

            //Inicializa a montagem do layout
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            //Recuperando a mensagem
            Mensagem mensagem = mensagens.get(position);
            //Monta a view a partir do arquivo xml
            //Verifica quem enviou a mensagem
            if (idUsuarioRemetente.equals(mensagem.getIdUsuario())){
                view = inflater.inflate(R.layout.item_mensagem_direita,parent,false);
            } else {
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }
            //Recupera o elemento TextView para exibicao
            TextView mesagemRecebida = (TextView) view.findViewById(R.id.mensagem_recebida_text_view);
            mesagemRecebida.setText(mensagem.getMensagem());
        } else {

        }

        return  view;
    }
}
