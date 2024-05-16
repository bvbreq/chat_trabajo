package com.dam.chat_trabajo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder> {

    private List<String> usuarios;
    private List<String> usuariosSeleccionados;


    public UsuariosAdapter(List<String> usuarios) {
        this.usuarios = usuarios;
        this.usuariosSeleccionados = new ArrayList<>();
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        String usuario = usuarios.get(position);
        holder.bindUsuario(usuario, usuariosSeleccionados);
    }
    public List<String> getUsuariosSeleccionados() {
        return usuariosSeleccionados;
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }


    // Método para actualizar la lista de usuarios seleccionados
    public void actualizarUsuariosFiltrados(List<String> nuevosUsuariosSeleccionados) {
        usuariosSeleccionados.clear();
        if (nuevosUsuariosSeleccionados != null) {
            usuariosSeleccionados.addAll(nuevosUsuariosSeleccionados);
        }
        notifyDataSetChanged(); // Notificar al RecyclerView de que los datos han cambiado
    }


    public class UsuarioViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBoxUsuario;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxUsuario = itemView.findViewById(R.id.checkBoxUsuario);
        }

        public void bindUsuario(String usuario, List<String> usuariosSeleccionados) {
            checkBoxUsuario.setText(usuario);
            checkBoxUsuario.setChecked(usuariosSeleccionados.contains(usuario));
            checkBoxUsuario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        usuariosSeleccionados.add(usuario);
                    } else {
                        usuariosSeleccionados.remove(usuario);
                    }
                }
            });
        }

    }
}
