package com.example.employeedb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.List;

public class EmployeeAdapter extends ArrayAdapter<Employee> {

    Context mCtx;
    int layoutRes;
    List<Employee> employeeList;
    DatabaseManager mDatabase;

    public EmployeeAdapter(Context mCtx, int layoutRes, List<Employee> employeeList, DatabaseManager mDatabase){

        super(mCtx, layoutRes, employeeList);
        this.mCtx = mCtx;
        this.layoutRes = layoutRes;
        this.employeeList = employeeList;
        this.mDatabase = mDatabase;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(layoutRes, null);
        //from list_layout_employee
        TextView textViewName = view.findViewById(R.id.tvName);
        TextView textViewDept = view.findViewById(R.id.tvDepartment);
        TextView textViewSalary = view.findViewById(R.id.tvSalary);
        TextView textViewJoinDate = view.findViewById(R.id.tvJoinDate);
        final Employee employee = employeeList.get(position);

        textViewName.setText(employee.getName());
        textViewDept.setText(employee.getDept());
        textViewSalary.setText(String.valueOf(employee.getSalary()));
        textViewJoinDate.setText(employee.getJoiningDate());

        view.findViewById(R.id.btnDeleteEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEmployee(employee);
            }
        });

        view.findViewById(R.id.btnEditEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAnEmployee(employee);
            }
        });
        return view;
    }//get view


    private void updateAnEmployee(final Employee employee){
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_employee, null);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //form dialog_update_employee.xml
        final EditText editTextName = view.findViewById(R.id.etName);
        final EditText editTextSalary = view.findViewById(R.id.etSalary);
        final Spinner spinner = view.findViewById(R.id.spinnerDepartment);

        editTextName.setText(employee.getName());
        editTextSalary.setText(String.valueOf(employee.getSalary()));

        view.findViewById(R.id.btAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String salary = editTextSalary.getText().toString();
                String dept = spinner.getSelectedItem().toString();

                if(name.isEmpty()){
                    editTextName.setError("Name can't be empty");
                    editTextName.requestFocus();
                    return;
                }

                if(mDatabase.updateEmployee(employee.getId(),name,dept,Double.valueOf(salary))) {
                    Toast.makeText(mCtx, "Employee Updated", Toast.LENGTH_SHORT).show();
                    loadEmployeesFromDatabaseAgain();
                }
                alertDialog.dismiss();
            }
        });
    }//update Employee

    private void deleteEmployee(final Employee employee){
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setTitle("Are you sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //calling the delete method from the database manager instance
                if (mDatabase.deleteEmployee(employee.getId())){
                    loadEmployeesFromDatabaseAgain();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }

    private void loadEmployeesFromDatabaseAgain() {

        Cursor cursor = mDatabase.getAllEmployees();

        employeeList.clear();
        if (cursor.moveToFirst()){
            do{
                employeeList.add(new Employee(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4)
                ));
            }while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }//load employees

}//employee adapter
