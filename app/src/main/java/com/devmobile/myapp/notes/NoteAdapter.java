package com.devmobile.myapp.notes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.devmobile.myapp.AppBase;
import com.devmobile.myapp.R;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private ArrayList<String> titles;
    private ArrayList<String> contents;
    private Activity activity;

    public NoteAdapter(Activity activity, ArrayList<String> titles, ArrayList<String> contents) {
        this.titles = titles;
        this.contents = contents;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTextView.setText(titles.get(position));
        holder.bodyTextView.setText(contents.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titles.get(holder.getAdapterPosition());
                String body = contents.get(holder.getAdapterPosition());

                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle(title);
                alert.setMessage(body);
                alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(activity, UpdateNoteActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("body", body);
                        activity.startActivity(intent);
                    }
                });
                alert.setNegativeButton("Close", null);
                alert.show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String title = titles.get(holder.getAdapterPosition());
                String body = contents.get(holder.getAdapterPosition());
                String username = activity.getSharedPreferences("MyAppPrefs", Activity.MODE_PRIVATE).getString("username", "");

                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Delete ?");
                alert.setMessage("Do you want to delete this note ?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String qu = "DELETE FROM NOTES WHERE title = '" + title + "' AND body = '" + body + "' AND username = '" + username + "'";
                        if (AppBase.handler.execAction(qu)) {
                            ((NoteActivity) activity).loadNotes();
                            Toast.makeText(activity, "Deleted", Toast.LENGTH_LONG).show();
                        } else {
                            ((NoteActivity) activity).loadNotes();
                            Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView bodyTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.noteTitle);
            bodyTextView = itemView.findViewById(R.id.noteBody);
        }
    }
}