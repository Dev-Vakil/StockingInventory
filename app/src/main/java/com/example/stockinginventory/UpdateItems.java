package com.example.stockinginventory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class UpdateItems extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText editAmount;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference inventoryRef = db.collection("Inventory");

    private final ArrayList<String> departmentIDL = new ArrayList<>();
    private final ArrayList<String> itemIDL = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_update_items, container, false);

        editAmount = v.findViewById(R.id.Amount);
        Spinner spinnerDepartment = v.findViewById(R.id.Spin_Depart);
        Spinner spinnerItem = v.findViewById(R.id.Spin_Item);
        Button submit = v.findViewById(R.id.button4);
        spinnerDepartment.setOnItemSelectedListener(this);
        spinnerItem.setOnItemSelectedListener(this);

        inventoryRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                Note note = documentSnapshot.toObject(Note.class);

                String deptId = note.getDepartmentId();
                departmentIDL.add(deptId);
            }
        });
        departmentIDL.add("Select Department");
        ArrayAdapter<String> dep = new ArrayAdapter<>(requireActivity().getApplicationContext(), R.layout.spinner_list, departmentIDL);
        dep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(dep);

        itemIDL.add("Select Department");
        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String deptid = departmentIDL.get(i);
                CollectionReference itemRef = inventoryRef.document(deptid).collection("department Items");
                itemRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    itemIDL.clear();
                    itemIDL.add("Select Item");
                    for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        ItemNote note = documentSnapshot.toObject(ItemNote.class);

                        String itemId = note.getItemId();
                        itemIDL.add(itemId);
                    }
                });

                ArrayAdapter<String> item = new ArrayAdapter<>(requireActivity().getApplicationContext(), R.layout.spinner_list, itemIDL);
                item.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerItem.setAdapter(item);

                submit.setOnClickListener(v -> {
                    String amountStr = editAmount.getText().toString();
                    String itemid = spinnerItem.getSelectedItem().toString();
                    //String deptId = spinnerDepartment.getSelectedItem().toString();
                    //DocumentReference itemRef = inventoryRef.document(deptid).collection("department Items")
                    if(deptid.equals("Select Department")){
                        Toast.makeText(getActivity(), "Please Select Department", Toast.LENGTH_SHORT).show();
                    }else{
                        if(itemid.equals("Select Item")){
                            Toast.makeText(getActivity(), "Please Select Item", Toast.LENGTH_SHORT).show();
                        }else{
                            itemRef.document(itemid).get().addOnSuccessListener(documentSnapshot -> {
                                ItemNote amountNote = documentSnapshot.toObject(ItemNote.class);
                                if(amountStr.equals("")){
                                    Toast.makeText(getActivity(), "Enter the Required amount", Toast.LENGTH_SHORT).show();
                                }else{
                                    int amount = Integer.parseInt(amountStr);
                                    if (amountNote != null) {
                                        int permAmount = amount + amountNote.getItemAmount();
                                        String itemInfo = amountNote.getItemInfo();
                                        int tempAmount = amount + amountNote.getTempItemAmount();
                                        ItemNote updateItem = new ItemNote(itemid, itemInfo, permAmount, tempAmount);
                                        itemRef.document(itemid).set(updateItem).addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Amount Updated", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getActivity(), "Amount not Updated", Toast.LENGTH_SHORT).show());
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "Amount not Available", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });

                            spinnerDepartment.setAdapter(dep);
                            spinnerItem.setAdapter(item);
                            editAmount.setText("");
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //submit.setOnClickListener(view -> Toast.makeText(getActivity(), "Please Fill in the values first", Toast.LENGTH_SHORT).show());
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}