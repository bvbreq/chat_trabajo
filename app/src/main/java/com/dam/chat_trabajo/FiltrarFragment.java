package com.dam.chat_trabajo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



    public class FiltrarFragment extends Fragment {

        private RecyclerView recyclerViewUsuarios;
        private UsuariosAdapter usuariosAdapter;
        private List<String> usuariosSeleccionados = new ArrayList<>();
        protected List<Sala> salasFiltradas = new ArrayList<>();


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_filtrar, container, false);

            recyclerViewUsuarios = view.findViewById(R.id.recyclerViewUsuarios);
            recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getActivity()));

            obtenerNombresUsuarios(usuarios -> {
                usuariosAdapter = new UsuariosAdapter(usuarios);
                recyclerViewUsuarios.setAdapter(usuariosAdapter);


            //List<String> usuariosMarcados = usuariosAdapter.getUsuariosSeleccionados();

            // Asignar la lista de usuarios marcados a usuariosSeleccionados en el fragmento
           // usuariosSeleccionados.clear();
            //usuariosSeleccionados.addAll(usuariosMarcados);
           // usuariosSeleccionados =
            Button botonListo = view.findViewById(R.id.buttonListo);
            botonListo.setOnClickListener(v -> {
                if (usuariosAdapter != null) {
                    // Obtener la lista de usuarios marcados del adaptador
                    List<String> usuariosMarcados = usuariosAdapter.getUsuariosSeleccionados();

                    // Verificar si la lista de usuarios marcados no es nula
                    if (usuariosMarcados != null) {
                        // Limpiar y actualizar la lista de usuarios seleccionados en el fragmento con los usuarios marcados
                        usuariosSeleccionados.clear();
                        usuariosSeleccionados.addAll(usuariosMarcados);

                        // Lógica para mostrar salas filtradas al presionar el botón "Listo"
                mostrarSalasFiltradas();

                // Remover el fragmento actual
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(this).commit(); // Remueve el fragmento actual

                // Crear una nueva transacción para agregar el nuevo fragmento
                FragmentTransaction newTransaction = fragmentManager.beginTransaction();
                FiltrarFragment nuevoFragmento = new FiltrarFragment();
                newTransaction.add(R.id.container, nuevoFragmento); // Agrega el nuevo fragmento
                newTransaction.addToBackStack(null); // Agrega la transacción al BackStack
                newTransaction.commit(); // Realiza la transacción
                    } else {
                        Toast.makeText(requireContext(), "Error: Lista de usuarios marcados es nula", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(requireContext(), "Error: Adaptador no inicializado correctamente", Toast.LENGTH_SHORT).show();

                }
            });
        });

            return view; // Devuelve la vista inflada
        }


            private void obtenerNombresUsuarios(OnSuccessListener<List<String>> onSuccessListener) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Set<String> usuariosSet = new HashSet<>(); // Utilizar un Set para garantizar usuarios únicos
            db.collection("chat")
                    .document("salas_aux")
                    .collection("salas")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String participantesString = document.getString("usuarios");
                            if (participantesString != null && !participantesString.isEmpty()) {
                                List<String> participantes = Arrays.asList(participantesString.split(","));
                                usuariosSet.addAll(participantes); // Agregar usuarios al Set
                            }
                        }
                        List<String> usuariosList = new ArrayList<>(usuariosSet); // Convertir Set a List
                        onSuccessListener.onSuccess(usuariosList);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FiltrarFragment", "Error al obtener lista de usuarios: " + e.getMessage());
                        onSuccessListener.onSuccess(new ArrayList<>()); // En caso de error, devolver lista vacía
                    });
        }

        private void mostrarSalasFiltradas() {
            obtenerSalasFiltradas(usuariosSeleccionados, salasFiltradas -> {
                if (usuariosAdapter != null) {
                    // Actualizar el adaptador con las salas filtradas o con los usuarios seleccionados
                    usuariosAdapter.actualizarUsuariosFiltrados(usuariosSeleccionados);
                    // Aquí puedes implementar la lógica para mostrar las salas filtradas
                }
            });
        }


        protected void obtenerSalasFiltradas(List<String> usuariosSeleccionados, OnSuccessListener<List<Sala>> onSuccessListener) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("chat")
                    .document("salas_aux")
                    .collection("salas")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        Set<String> salasUnicas = new HashSet<>();
                        List<Sala> salasFiltradas = new ArrayList<>();

                        Log.d("FiltrarFragment", "Número de documentos obtenidos: " + queryDocumentSnapshots.size());

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String idSala = document.getId();
                            String nombreSala = document.getString("nombre");
                            String participantesString = document.getString("usuarios");
                            String admin = document.getString("admin");
                            Long imagenLong = document.getLong("imagen");
                            int imagen = (imagenLong != null) ? imagenLong.intValue() : 0;

                            List<String> participantes = new ArrayList<>();
                            if (participantesString != null && !participantesString.isEmpty()) {
                                participantes = Arrays.asList(participantesString.split(","));
                            }

                            boolean contieneUsuario = false;
                            for (String usuario : usuariosSeleccionados) {
                                if (participantes.contains(usuario)) {
                                    contieneUsuario = true;
                                    break;
                                }
                            }

                            if (contieneUsuario) {
                                String salaKey = String.join(",", participantes);
                                if (!salasUnicas.contains(salaKey)) {
                                    salasUnicas.add(salaKey);
                                    Sala sala = new Sala(idSala, nombreSala, participantes, admin, imagen);
                                    salasFiltradas.add(sala);
                                }
                            }
                        }

                        Log.d("FiltrarFragment", "Número de salas filtradas: " + salasFiltradas.size());
                        onSuccessListener.onSuccess(salasFiltradas); // Devolver la lista de salas filtradas

                    })
                    .addOnFailureListener(e -> {
                        Log.e("FiltrarFragment", "Error al obtener lista de salas: " + e.getMessage());
                        onSuccessListener.onSuccess(new ArrayList<>()); // En caso de error, devolver lista vacía
                    });
        }


    }
