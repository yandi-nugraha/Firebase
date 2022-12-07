package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText nameEditText, addressEditText, updatedNameEditText, updatedAddressEditText;

    DatabaseReference databaseReference;

    Student student;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference(Student.class.getSimpleName());

        nameEditText = findViewById(R.id.name_editText);
        addressEditText = findViewById(R.id.address_editText);
        updatedNameEditText = findViewById(R.id.update_name_editText);
        updatedAddressEditText = findViewById(R.id.update_address_editText);

        findViewById(R.id.insert_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        findViewById(R.id.read_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });

        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });
    }

    private void insertData() {
        Student student = new Student();
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();

        if (name != "" && address != "") {
            student.setName(name);
            student.setAddress(address);

            databaseReference.push().setValue(student);
            Toast.makeText(this, "Successfully insert data!", Toast.LENGTH_SHORT).show();
        }
    }

    private void readData() {
        student = new Student();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot currentData : snapshot.getChildren()) {
                        key = currentData.getKey();
                        student.setName(currentData.child("name").getValue().toString());
                        student.setAddress(currentData.child("address").getValue().toString());
                    }
                }

                updatedNameEditText.setText(student.getName());
                updatedAddressEditText.setText(student.getAddress());
                Toast.makeText(MainActivity.this, "Data has been shown!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateData() {
        Student updatedData = new Student();
        updatedData.setName(updatedNameEditText.getText().toString());
        updatedData.setAddress(updatedAddressEditText.getText().toString());
        // Untuk update
        databaseReference.child(key).setValue(updatedData);
    }

    private void deleteData() {
        // Untuk delete
        databaseReference.child(key).removeValue();
    }
}