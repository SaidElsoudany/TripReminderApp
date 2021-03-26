package com.elsoudany.said.tripreminderapp.AddNotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elsoudany.said.tripreminderapp.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHold>
{
    private Context context;
    List<String> notes;

    public NoteAdapter(Context context, List<String> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup recycleView, int viewType)

    {
        LayoutInflater inflater =LayoutInflater.from(recycleView.getContext());
        View v=inflater.inflate(R.layout.note_row,recycleView,false);
        ViewHold vh=new ViewHold(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position)
    {
        holder.noteValue.setText(notes.get(position));
        holder.deleteNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notes.remove(position);
                notifyItemRemoved(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder
    {
        TextView noteValue;
        ImageView deleteNoteBtn;
        public View layout;


        public ViewHold(@NonNull View itemView)
        {
            super(itemView);
            layout=itemView;
           noteValue=itemView.findViewById(R.id.txt_noteValue);
           deleteNoteBtn=itemView.findViewById(R.id.btn_deleteNote);
        }
    }
}
