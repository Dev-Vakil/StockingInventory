package com.example.stockinginventory;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;



public class InsertDeleteActivity extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Insert DeleteActivity";

    private EditText editTextInfo;
    private EditText editTextItem;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference inventoryRef = db.collection("Inventory");

    private final String[] insdel = {"Select for Insert or Delete","Insert", "Delete"};
    private final ArrayList<String> departmentIDL = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_insert_delete, container, false);
        editTextInfo = v.findViewById(R.id.editTextTextPersonName10);
        editTextInfo = v.findViewById(R.id.editTextTextPersonName10);
        editTextItem = v.findViewById(R.id.editTextTextPersonName9);
        Spinner spinnerDepartment = v.findViewById(R.id.spinner4);
        Spinner spinnerInsertDelete = v.findViewById(R.id.spinner3);
        Button submit = v.findViewById(R.id.button2);
        spinnerInsertDelete.setOnItemSelectedListener(this);
        spinnerDepartment.setOnItemSelectedListener(this);

        inventoryRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                Note note = documentSnapshot.toObject(Note.class);

                String deptId = note.getDepartmentId();
                departmentIDL.add(deptId);
            }
        });
        departmentIDL.add("Select Department");
        ArrayAdapter<String> id = new ArrayAdapter<>(requireActivity().getApplicationContext(), R.layout.spinner_list, insdel);
        id.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInsertDelete.setAdapter(id);


        ArrayAdapter<String> dep = new ArrayAdapter<>(requireActivity().getApplicationContext(), R.layout.spinner_list, departmentIDL);
        dep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(dep);

        submit.setOnClickListener(view -> {
            String item = editTextItem.getText().toString();
            String info = editTextInfo.getText().toString();
            String deptid = spinnerDepartment.getSelectedItem().toString();
            String insert_delete = spinnerInsertDelete.getSelectedItem().toString();
            ItemNote note = new ItemNote(item.toUpperCase(), info, 0, 0);
            DocumentReference itemRef = inventoryRef.document(deptid).
                    collection("department Items").document(item.toUpperCase());



            switch (insert_delete) {
                case "Insert":
                    if (deptid.equals("Select Department"))
                        Toast.makeText(getActivity(), "Enter Proper Values", Toast.LENGTH_SHORT).show();
                    else {
                        if (item.equals("Item Name")) {
                            Toast.makeText(getActivity(), "Enter Proper Values", Toast.LENGTH_SHORT).show();
                        } else {
                            itemRef.set(note).addOnSuccessListener(unused ->
                                    Toast.makeText(getActivity(), "Item Added", Toast.LENGTH_SHORT).show()
                            ).addOnFailureListener(e -> {
                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            });
                        }
                    }

                    break;
                case "Delete":
                    if (deptid.equals("Select Department"))
                        Toast.makeText(getActivity(), "Enter Proper Values", Toast.LENGTH_SHORT).show();
                    else {
                        if (item.equals("Item Name")) {
                            Toast.makeText(getActivity(), "Enter Proper Values", Toast.LENGTH_SHORT).show();
                        } else {
                            inventoryRef.document(deptid).
                                    collection("department Items")
                                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                int count = 0;
                                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                    ItemNote itemNote = documentSnapshot.toObject(ItemNote.class);

                                    String itemId = itemNote.getItemId();
                                    if(itemId.equals(item.toUpperCase()))
                                    {
                                        count++;
                                    }
                                }
                                if (count == 0) {

                                    Toast.makeText(getActivity(), "Item does not exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    itemRef.delete();
                                    Toast.makeText(getActivity(), "Item Deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    break;
                case "Select for Insert or Delete":
                    if (deptid.equals("Select Department"))
                        Toast.makeText(getActivity(), "Enter Proper Values", Toast.LENGTH_SHORT).show();
                    else {
                        if (item.equals("Item Name")) {
                            Toast.makeText(getActivity(), "Enter Proper Values", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Please enter Proper values", Toast.LENGTH_SHORT).show();
                        }
                    }

                    break;
            }
            editTextItem.setText("Item Name");
            editTextInfo.setText("");
            spinnerInsertDelete.setAdapter(id);
            spinnerDepartment.setAdapter(dep);
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
