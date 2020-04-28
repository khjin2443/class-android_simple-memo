package com.example.hasoo.simplememo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteSearchAdapter extends RecyclerView.Adapter<NoteSearchAdapter.ViewHolder>{
    ArrayList<NoteList> items = new ArrayList<NoteList>();
    final Intent intent;
    private String sql;

    //    인텐트 정보 받고 문자형 변수 sql 초기화.
    NoteSearchAdapter(Intent intent) {
        this.intent = intent;
        sql = intent.getExtras().getString("sql");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.activity_noteadapter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteList item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(NoteList item) {
        items.add(item);
    }

    public void setItems(ArrayList<NoteList> items) {
        this.items = items;
    }

    public NoteList getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, NoteList item) {
        items.set(position, item);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_title;
        TextView textView_content;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_title = itemView.findViewById(R.id.adapter_tit);
            textView_content = itemView.findViewById(R.id.adapter_con);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper dbHelper;
                    SQLiteDatabase database;

                    dbHelper = new DatabaseHelper(v.getContext());
                    database = dbHelper.getWritableDatabase();

                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        Log.d("tag", position + "클릭");
                        Cursor cursor = database.rawQuery(sql, null);
//                        전달받은 쿼리문을 저장한 문자열 변수 sql을 사용
                        cursor.move(position+1);
                        Intent intent = new Intent(v.getContext(),Edit_Notepad.class);
                        intent.putExtra("title",cursor.getString(1));
                        intent.putExtra("content",cursor.getString(2));
                        intent.putExtra("position", cursor.getInt(0));
                        Context context = v.getContext();
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                }
            });
        }

        public void setItem(NoteList item) {
            textView_title.setText(item.getTitle());
            textView_content.setText(item.getContent());
        }
    }
}
