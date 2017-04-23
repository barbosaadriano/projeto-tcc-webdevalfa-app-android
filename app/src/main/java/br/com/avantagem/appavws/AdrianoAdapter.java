package br.com.avantagem.appavws;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrador on 21/04/2017.
 */

public class AdrianoAdapter extends BaseAdapter{
    private final List<EventosListaModelo> eventos;
    private final Activity act;

    public AdrianoAdapter(List<EventosListaModelo> eventos, Activity act) {
        this.eventos = eventos;
        this.act = act;
    }

    @Override
    public int getCount() {
        return  eventos.size();
    }

    @Override
    public Object getItem(int position) {
        return eventos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater()
                .inflate(R.layout.lista_adriano_layout, parent, false);
        EventosListaModelo evento = eventos.get(position);
        TextView descricao = (TextView)
                view.findViewById(R.id.tvDescricao);
        TextView momento = (TextView)
                view.findViewById(R.id.tvMomento);
        TextView dados = (TextView)
                view.findViewById(R.id.tvDados);
        TextView dispositivo = (TextView)
                view.findViewById(R.id.tvDispositivo);
        TextView local = (TextView)
                view.findViewById(R.id.tvLocal);
        descricao.setText(evento.getDescricao());
        momento.setText(evento.getMomento());
        dados.setText(evento.getDados());
        dispositivo.setText(evento.getDispositivo());
        local.setText(evento.getLocal());
        return view;
    }
}
