package com.elsoudany.said.tripreminderapp.AddNotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.elsoudany.said.tripreminderapp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AddNoteActivity extends AppCompatActivity {
    Button  btnAddNoteDialog,btnCancelNoteDialog;
    ImageButton backButton,btnAddNotes;
    AlertDialog alertDialog;
    LayoutInflater inflater;
    EditText addNoteText;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<String> notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
      notes=new ArrayList<>();
        recyclerView=findViewById(R.id.noteRecyclerView);

        btnAddNotes=findViewById(R.id.btn_addNewNote);
        backButton=findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alertDialog = new AlertDialog.Builder(AddNoteActivity.this).create();
                  inflater = LayoutInflater.from(getApplicationContext());
                View dialogView = inflater.inflate(R.layout.add_note_dialog, null);
                btnAddNoteDialog= dialogView.findViewById(R.id.btn_addNoteDialog);
                btnCancelNoteDialog=dialogView.findViewById(R.id.btn_cancelNoteDialog);
                addNoteText=dialogView.findViewById(R.id.txt_addNoteDialog);
                btnAddNoteDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notes.add(addNoteText.getText().toString());
                        adapter=new NoteAdapter(AddNoteActivity.this,notes);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AddNoteActivity.this));
                        recyclerView.setAdapter(adapter);

                        Toast.makeText(AddNoteActivity.this, ""+addNoteText.getText(), Toast.LENGTH_SHORT).show();
                        alertDialog.cancel();
                    }
                });
                btnCancelNoteDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        alertDialog.cancel();
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();

            }
        });
    }
}