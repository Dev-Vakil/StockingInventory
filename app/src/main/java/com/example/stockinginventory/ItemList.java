package com.example.stockinginventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ItemList extends Fragment implements AdapterView.OnItemSelectedListener {

    private final String deptId;

    public ItemList(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptId() {
        return deptId;
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference inventoryRef = db.collection("Inventory");

    private final ArrayList<String> departmentIDL = new ArrayList<>();
    private final ArrayList<String> itemIDL = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_item_list, container, false);

        Spinner spinnerDepartment = v.findViewById(R.id.Spin_Department);
        ListView listItem = v.findViewById(R.id.item_list);
        spinnerDepartment.setOnItemSelectedListener(this);

        inventoryRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Note note = documentSnapshot.toObject(Note.class);

                String deptId = note.getDepartmentId();
                departmentIDL.add(deptId);
            }
        });
        departmentIDL.add("Select Department");
        ArrayAdapter<String> id = new ArrayAdapter<>(requireActivity().getApplicationContext(), R.layout.spinner_list, departmentIDL);
        id.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(id);



        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String deptid = departmentIDL.get(i);


                itemIDL.clear();
                inventoryRef.document(deptid).collection("department Items").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        ItemNote itemNote = documentSnapshot.toObject(ItemNote.class);

                        String deptId = itemNote.getItemId();
                        itemIDL.add(deptId);
                    }
                    ArrayAdapter<String> id = new ArrayAdapter<>(requireActivity().getApplicationContext(), R.layout.spinner_list, itemIDL);
                    listItem.setAdapter(id);
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}