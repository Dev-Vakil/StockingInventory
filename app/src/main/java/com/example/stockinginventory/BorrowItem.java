package com.example.stockinginventory;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class BorrowItem extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText editBorrowerName;
    private EditText editBorrowerNumber;
    private EditText editAmount;
    private EditText editReason;
    private Calendar calendar;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference inventoryRef = db.collection("Inventory");
    private final CollectionReference historyRef = db.collection("History");

    private final ArrayList<String> departmentIDL = new ArrayList<>();
    private final ArrayList<String> itemIDL = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.borrow_item, container, false);

        editBorrowerName = v.findViewById(R.id.editTextTextPersonName2);
        editBorrowerNumber = v.findViewById(R.id.editTextTextPersonName4);
        editAmount = v.findViewById(R.id.editTextTextPersonName5);
        editReason = v.findViewById(R.id.editTextTextPersonName6);
        Spinner spinnerDepartment = v.findViewById(R.id.spinner);
        Spinner spinnerItem = v.findViewById(R.id.spinner2);
        Button btn_date = v.findViewById(R.id.due_Date);
        Button submit = v.findViewById(R.id.button);
        spinnerDepartment.setOnItemSelectedListener(this);
        spinnerItem.setOnItemSelectedListener(this);
        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
               calendar.set(Calendar.YEAR, i);
               calendar.set(Calendar.MONTH, i1);
               calendar.set(Calendar.DAY_OF_MONTH, i2);

               updateCalendar();
            }

            private void updateCalendar(){
                String Format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);

                btn_date.setText(sdf.format(calendar.getTime()));
            }
        };

        btn_date.setOnClickListener(view -> {
           new DatePickerDialog(getActivity(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
//            datePickerDialog  DatePickerDialog datePickerDialog =
        });

        inventoryRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
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
            @SuppressLint("SetTextI18n")
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

                ArrayAdapter<String> item = new ArrayAdapter<>(requireActivity().getApplicationContext(), R.layout.spinner_list, itemIDL);//color from  spinner_list
                item.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerItem.setAdapter(item);

                submit.setOnClickListener(view12 -> {

                    String name = editBorrowerName.getText().toString();
                    String number = editBorrowerNumber.getText().toString();
                    String amountStr = editAmount.getText().toString();
                    String reason = editReason.getText().toString();
                    String date = btn_date.getText().toString();
                    String itemId = spinnerItem.getSelectedItem().toString();

                    itemRef.document(itemId).get().addOnSuccessListener(documentSnapshot -> {
                        ItemNote itemNoteGet = documentSnapshot.toObject(ItemNote.class);

                        if (itemNoteGet != null) {
                            if (deptid.equals("Select Department")){
                                Toast.makeText(getActivity(), "Please select the department", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(itemId.equals("Select Item")){
                                    Toast.makeText(getActivity(), "Please select the item", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if (name.equals("")){
                                        Toast.makeText(getActivity(), "Please Enter your name", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        if (number.length() != 10){
                                            Toast.makeText(getActivity(), "Please Enter your number properly", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            if (amountStr.equals("")){
                                                Toast.makeText(getActivity(), "Please Enter the amount", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                if(date.equals("Set Date")){
                                                    Toast.makeText(getActivity(), "Please select a proper due date", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    String itemInfo = itemNoteGet.getItemInfo();
                                                    int itemAmount = itemNoteGet.getItemAmount();
                                                    int amount = Integer.parseInt(amountStr);
                                                    int tempItemAmount = itemNoteGet.getTempItemAmount();
                                                    tempItemAmount -= amount;
                                                    DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss", Locale.US);
                                                    Date dateRef = new Date();
                                                    String dateId = dateFormat.format(dateRef);
                                                    HistoryNote note = new HistoryNote(dateId, name, number, reason, amount, date, itemId);
                                                    ItemNote itemNote = new ItemNote(itemId, itemInfo, itemAmount, tempItemAmount);
                                                    if(tempItemAmount<=0){
                                                        Toast.makeText(getActivity(), "There is no amount left to borrow", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        itemRef.document(itemId).set(itemNote).addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Amount Updated", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getActivity(), "Amount not Updated", Toast.LENGTH_SHORT).show());
                                                        historyRef.document(dateId).set(note).addOnSuccessListener(unused -> Toast.makeText(getActivity(), "History Added", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getActivity(),"failed",Toast.LENGTH_SHORT).show());
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    });

                    editBorrowerName.setText("");
                    editBorrowerNumber.setText("");
                    editAmount.setText("");
                    editReason.setText("");
                    btn_date.setText("Set Date");
                    spinnerDepartment.setAdapter(dep);
                    spinnerItem.setAdapter(item);
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
