package com.example.hasoo.simplememo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    String tableName;
    String dbName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbName = "notepad.db";
        tableName = "noteData";

        createDatabase(); // 데이터베이스 생성 함수
        createTable(tableName); // 테이블 생성 함수
        executeQuery();

//        플로팅버튼 터치시 메모작성 클래스 호출
        Button newbutton = findViewById(R.id.btn_new);
        newbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewNotepad.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void createDatabase() {
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        // 데이터베이스 생성 쓰기 가능한 상태로 설정
    }

    private void createTable(String name) {
        database.execSQL("create table if not exists " + name + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " title text, "
                + " content text)");
//        메모내용을 담을 테이블 생성
    }

    public void executeQuery() {
        Cursor cursor = database.rawQuery("select _id, title, content from noteData", null);
//        select 컬럼 from 테이블 order by 컬럼 내림차순
//        noteData 테이블에서 _id, title, content 컬럼을 time 컬럼을 기준으로 내림차순으로
        int recordCount = cursor.getCount();

        NoteAdapter adapter = new NoteAdapter();
//         어댑터 설정
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//         리싸이클뷰 설정
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            adapter.addItem(new NoteList(title, content));
//             어댑터에 아이템 추가
        }

        RecyclerDecoration spaceDecoration = new RecyclerDecoration(20);
        recyclerView.addItemDecoration(spaceDecoration);
//         아이템간의 간격 설정

        recyclerView.setAdapter(adapter);
//         리사이클뷰 어댑터 설정

        cursor.close();
    }

}