package com.example.stockinginventory;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class DepartmentActivity extends Fragment {
    private static final String TAG = "DepartmentActivity";

    private EditText editTextDepartment;
    private EditText editTextInformation;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference inventoryRef = db.collection("Inventory");

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_department, container, false);
        editTextDepartment = v.findViewById(R.id.department_AddDep);
        editTextInformation = v.findViewById(R.id.info_AddDep);
        Button submit = v.findViewById(R.id.button3);

        submit.setOnClickListener(view -> {
            String department = editTextDepartment.getText().toString();
            String information = editTextInformation.getText().toString();

            Note note = new Note(information, department.toUpperCase());

            inventoryRef.document(department.toUpperCase()).set(note)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(getActivity(), "Department Added",
                                    Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), "Error!",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            });
            editTextDepartment.setText("");
            editTextInformation.setText("");
        });
        return v;
    }
}
