package com.example.week08midterm;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText etStudentName, etStudentID;
    CheckBox cbHasEnrolled;
    Button btnAdd, btnEdit, btnDelete, btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Database Helper
        myDb = new DatabaseHelper(this);

        // Link UI elements from XML
        etStudentName = findViewById(R.id.etStudentName);
        etStudentID = findViewById(R.id.etStudentID);
        cbHasEnrolled = findViewById(R.id.cbHasEnrolled);

        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);

        // Activate Button Listeners
        addData();
        editData();
        deleteData();
        viewAllData();
    }

    public void addData() {
        btnAdd.setOnClickListener(v -> {
            String id = etStudentID.getText().toString().trim();
            String name = etStudentName.getText().toString().trim();
            boolean isEnrolled = cbHasEnrolled.isChecked();

            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter both Student Name and ID", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isInserted = myDb.insertStudent(id, name, isEnrolled);
            if (isInserted) {
                Toast.makeText(MainActivity.this, "Student Added Successfully", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(MainActivity.this, "Error: Student ID already exists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editData() {
        btnEdit.setOnClickListener(v -> {
            String id = etStudentID.getText().toString().trim();
            String name = etStudentName.getText().toString().trim();
            boolean isEnrolled = cbHasEnrolled.isChecked();

            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(MainActivity.this, "Enter the existing Student ID and updated Name", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isUpdated = myDb.updateStudent(id, name, isEnrolled);
            if (isUpdated) {
                Toast.makeText(MainActivity.this, "Student Record Updated", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(MainActivity.this, "Student ID not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteData() {
        btnDelete.setOnClickListener(v -> {
            String id = etStudentID.getText().toString().trim();

            if (id.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter the Student ID to delete", Toast.LENGTH_SHORT).show();
                return;
            }

            Integer deletedRows = myDb.deleteStudent(id);
            if (deletedRows > 0) {
                Toast.makeText(MainActivity.this, "Student Record Deleted", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(MainActivity.this, "Student ID not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void viewAllData() {
        btnView.setOnClickListener(v -> {
            Cursor res = myDb.getAllStudents();
            if (res.getCount() == 0) {
                showMessage("Empty", "No student records found.");
                return;
            }

            StringBuilder buffer = new StringBuilder();
            while (res.moveToNext()) {
                String id = res.getString(0);
                String name = res.getString(1);
                boolean enrolled = res.getInt(2) == 1;

                buffer.append("Student ID: ").append(id).append("\n");
                buffer.append("Student Name: ").append(name).append("\n");
                buffer.append("Enrolled: ").append(enrolled ? "Yes" : "No").append("\n\n");
            }

            showMessage("Student Records", buffer.toString());
        });
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void clearFields() {
        etStudentName.setText("");
        etStudentID.setText("");
        cbHasEnrolled.setChecked(false);
    }
}