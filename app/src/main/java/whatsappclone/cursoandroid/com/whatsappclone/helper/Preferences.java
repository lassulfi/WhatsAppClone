package whatsappclone.cursoandroid.com.whatsappclone.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Helper para salvar as opcoes no SharedPreferences do App
 * Created by LuisDaniel on 26/07/2017.
 */

public class Preferences {

    private Context context;
    private SharedPreferences sharedPreferences;
    private final String FILE_NAME = "whatsAppClone.Preferences";
    private int MODE = 0;
    private SharedPreferences.Editor editor;
    private final String KEY_IDENTIFIER = "identificadorUsuarioLogado";
    private final String KEY_USERNAME = "nomeUsuarioLogado";
    /*
    private final String KEY_NAME = "username";

    private final String KEY_PHONE = "telefone";
    private final String KEY_TOKEN = "token";
    */

    //Construtor da classe
    public Preferences(Context contextPreferences){

        context = contextPreferences;
        sharedPreferences = context.getSharedPreferences(FILE_NAME,MODE);
        editor = sharedPreferences.edit();

    }

    /**
     * Método para salvar as preferencias do usuário no arquivo do sharedPreferences
     * @param identificadorUsuario nome do usuário
     */

    /*public void  saveUserPreferences(String username, String telefone, String token){

        editor.putString(KEY_NAME,username);

        editor.putString(KEY_PHONE,telefone);
        editor.putString(KEY_TOKEN,token);


        editor.commit();

    } */
    public void  saveUserPreferences(String identificadorUsuario, String nomeUsuario){

        editor.putString(KEY_IDENTIFIER,identificadorUsuario);
        editor.putString(KEY_USERNAME,nomeUsuario);

        editor.commit();

    }

    public String getIdentificador(){
        return sharedPreferences.getString(KEY_IDENTIFIER, null);
    }

    public String getUsername(){
        return sharedPreferences.getString(KEY_USERNAME, null);
    }


    /**
     * Método para retornar os dados salvos no arquivo sharedPreferences do App.
     * @return userData HashMap com os dados do usuarios
     */
    /*
    public HashMap<String, String> getUserData(){

        //Instanciando a lista HashMap
        HashMap<String, String> userData = new HashMap<>();

        //Recuperando os dados do arquivo SharedPreferences
        userData.put(KEY_NAME, sharedPreferences.getString(KEY_NAME,null));
        userData.put(KEY_PHONE, sharedPreferences.getString(KEY_PHONE,null));
        userData.put(KEY_TOKEN, sharedPreferences.getString(KEY_TOKEN,null));

        return userData;

    }
    */

}
