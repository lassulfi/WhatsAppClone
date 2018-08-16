package whatsappclone.cursoandroid.com.whatsappclone.helper;

import android.util.Base64;

/**
 * Classe para codificacao e decodificacao do email do usuario para Base64
 * Created by LuisDaniel on 31/07/2017.
 */

public class Base64Custom {

    /**
     * Método para codificacao de um texto em Base64
     * @param texto texto a ser convertido
     * @return texto convertido
     */
    public static String encodeBase64(String texto){

        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");

    }

    public static String decodeBase64(String textoCodificado){

        return new String(Base64.decode(textoCodificado,Base64.DEFAULT));

    }

}
