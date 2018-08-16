package whatsappclone.cursoandroid.com.whatsappclone.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Classe de configuracao do Firebase - NAO PODE SER EXTENDIDA
 * Created by LuisDaniel on 28/07/2017.
 */

public final class FirebaseConfig {

    //Atributos
    private static DatabaseReference databaseReference;
    private static FirebaseAuth auth;

    //Métodos

    /**
     * Método para recuperar a instacia do Firebase
     * @return databaseReference
     */
    public static DatabaseReference getFirebase(){
        //Caso a instancia do Firebase não exista, criar uma nova referencia
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    /**
     * Método para recuperar a instancia de autenticacao no Firebase
     * @return auth
     */
    public static FirebaseAuth getFirebaseAuth(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

}
