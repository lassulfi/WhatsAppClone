package whatsappclone.cursoandroid.com.whatsappclone.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe para realizar as permissoes necessarias ao App
 * Android versão 6.0 ou superior
 * Created by LuisDaniel on 26/07/2017.
 */

public class WhatsAppPermissions {

    /**
     * Método estático para validar as permissoes no app
     * @param activity a atividade em que a solicitacao é solicitada
     * @param permissions Array de permissoes
     * @return
     */
    public static boolean validatedPermissions(int requestCode, Activity activity, String[] permissions){

        //Verificacao da versão do android
        if (Build.VERSION.SDK_INT >= 23){
            //Criando uma lista de permissoes que armazena as permissoes que necessitam ser liberadas
            List<String> permissionList = new ArrayList<String>();
            //Corre por todas as permissoes passadas, verificando uma a uma se já está liberada
            for (String permisson : permissions){
                //Verificando se a permissao está liberada a nivel do pacote de instalacao do app
               boolean validatePermission = ContextCompat.checkSelfPermission(activity,permisson) ==
                       PackageManager.PERMISSION_GRANTED;
                //Adicinando a permissao a lista caso ela não exista.
                if(!validatePermission) permissionList.add(permisson);
            }
            //Caso a lista de permissoes esteja vazia não é necessario verificar.
            if (permissionList.isEmpty()) return true;
            //Convertendo a List em um Array de Strings
            String[] newPermissions = new String[permissionList.size()];
            permissionList.toArray(newPermissions);
            //Solicita as permissoes necessárias
            ActivityCompat.requestPermissions(activity,newPermissions,requestCode);
        }

        return true;

    }

}
