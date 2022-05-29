package com.example.stockinginventory;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Department_Info extends Fragment {

    private final String deptId;
    public Department_Info(String deptId){
        this.deptId = deptId;
    }

    public String getDeptId() {
        return deptId;
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference departmentRef = db.collection("Inventory");

    private String info;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_department_info, container, false);

        TextView txtDepartmentName = v.findViewById(R.id.department_title);
        TextView txtDepartmentInfo = v.findViewById(R.id.info);
        Button btnDelete = v.findViewById(R.id.delete);
        Button btnItemList = v.findViewById(R.id.item_list);

        String dept = getDeptId();
        txtDepartmentName.setText(dept);

        departmentRef.document(dept).get().addOnSuccessListener(documentSnapshot -> {
            Note note = documentSnapshot.toObject(Note.class);

            assert note != null;
            info = note.getDepartmentInfo();
        });

        txtDepartmentInfo.setText(info);
        btnDelete.setOnClickListener(view -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
            alertDialogBuilder.setMessage("Are you sure!");
            alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, j) -> departmentRef.document(dept).delete().addOnSuccessListener(unused -> {
                Toast.makeText(requireActivity().getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(requireActivity().getApplicationContext(), MainActivity.class);
                i.putExtra("destination", "DepartmentList");
                startActivity(i);
            }));
            alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        btnItemList.setOnClickListener(view -> {
            Intent i = new Intent(requireActivity().getApplicationContext(), MainActivity.class);
            i.putExtra("deptIdItemList", dept);
            startActivity(i);
        });

        return v;
    }

}