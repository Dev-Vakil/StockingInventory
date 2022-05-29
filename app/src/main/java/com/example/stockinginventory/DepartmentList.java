package com.example.stockinginventory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DepartmentList extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference inventoryRef = db.collection("Inventory");

    private final ArrayList<String> departmentIDL = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_department_list, container, false);

        ListView listDepartment = v.findViewById(R.id.department_list);

        inventoryRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Note note = documentSnapshot.toObject(Note.class);

                String deptId = note.getDepartmentId();
                departmentIDL.add(deptId);
            }
            ArrayAdapter<String> id = new ArrayAdapter<>(requireActivity().getApplicationContext(), R.layout.spinner_list, departmentIDL);
            listDepartment.setAdapter(id);

            listDepartment.setOnItemClickListener((adapterView, view, j, l) -> {
                String deptId = departmentIDL.get(j);
                Intent i = new Intent(requireActivity().getApplicationContext(), MainActivity.class);
                i.putExtra("deptIdDepartmentInfo", deptId);
                startActivity(i);
            });
        });

        return v;
    }
}
