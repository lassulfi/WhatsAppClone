package whatsappclone.cursoandroid.com.whatsappclone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import whatsappclone.cursoandroid.com.whatsappclone.fragment.ContatosFragment;
import whatsappclone.cursoandroid.com.whatsappclone.fragment.ConversasFragment;

/**
 * Created by LuisDaniel on 31/07/2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    //Atributos
    private String[] tituloTabs = {"CONVERSAS", "CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Método para instanciar os fragmentos
     * @param position posição desejada para instanciar
     * @return fragment fragmento instanciado
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        //Carregando os fragmentos
        switch (position){
            case 0: //Instancia o fragmento CONVERSAS
                fragment = new ConversasFragment();
                break; //Instancia o fragmento CONTATOS
            case 1:
                fragment = new ContatosFragment();
                break;
        }

        return fragment;
    }

    /**
     * Retorna a quantidade de abas
     * @return tituloTabs.length quantidade de abas
     */
    @Override
    public int getCount() {
        return tituloTabs.length;
    }

    /**
     * Método que recupera o título da aba
     * @param position posicao da aba desejada
     * @return tituloTabs[position] titulo baseada na position
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return tituloTabs[position];
    }
}
