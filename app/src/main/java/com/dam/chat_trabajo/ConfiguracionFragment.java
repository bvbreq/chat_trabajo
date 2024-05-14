package com.dam.chat_trabajo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfiguracionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfiguracionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText editTextNewUsername;
    private Button buttonSave;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfiguracionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfiguracionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfiguracionFragment newInstance(String param1, String param2) {
        ConfiguracionFragment fragment = new ConfiguracionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_change_username, container, false);

        // Obtener referencias a las vistas del fragmento
        editTextNewUsername = view.findViewById(R.id.editTextNewUsername);
        buttonSave = view.findViewById(R.id.buttonSave);

        // Configurar el clic del botón para guardar el nuevo nombre de usuario
        buttonSave.setOnClickListener(v -> saveNewUsername());

        return view;    }

    private void saveNewUsername() {
        String newUsername = editTextNewUsername.getText().toString().trim();

        // Aquí debes implementar la lógica para guardar el nuevo nombre de usuario
        // Por ejemplo, actualiza el nombre de usuario en Firestore o en la autenticación de Firebase

        // Cierra el fragmento después de guardar exitosamente
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}