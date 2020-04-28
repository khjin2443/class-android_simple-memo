package com.example.hasoo.simplememo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    String tableName;
    String dbName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //데이터베이스 생성
        dbName = "notepad.db";
        tableName = "noteData";
        createDatabase();
        createTable(tableName);
        executeQuery();

        Button btn_new = findViewById(R.id.btn_new);
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewNotepad.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void searchQuery(String query) {
        String sql = "select * from noteData " +
                "where content Like " + "'%" + query + "%'" + "or title Like " + "'%" + query + "%'" + "order by time DESC";
//        sql문
//        select * from noteData where content Like '%query%' or title Like '%query%' order by time DESC"
//        select 컬럼 from 테이블 | *는 모든것을 포함
//        where 조건
//        content 컬럼내에서 Like(포함하는것) | title 컬럼도 동일함
//        입력값이 사과일때
//        %query면 썩은사과, 파인사과 등
//        query%면 사과가격, 사과하세요 등
//        %query%면 황금사과가격, 빨리사과하세요 등
//        order by 정렬 | time 컬럼을 기준으로 DESC 내림차순
//        order by를 사용하지 않거나 order by time ASC로 하면 오름차순

        Intent intent = new Intent(MainActivity.this, NoteSearchAdapter.class);
        intent.putExtra("sql", sql);
//        NoteSearchAdapter에 sql문을 전달.

        Cursor cursor = database.rawQuery("select * from noteData " +
                "where content Like " + "'%" + query + "%'" + "or title Like " + "'%" + query + "%'" + "order by time DESC", null);
//        위 문자열 sql과 쿼리 sql문은 같음

        int recordCount = cursor.getCount();

        NoteSearchAdapter adapter = new NoteSearchAdapter(intent);

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

        recyclerView.setAdapter(adapter);
//         리사이클뷰 어댑터 설정

        cursor.close();
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
                + " content text, "
                + " time text)");
//        메모내용을 담을 테이블 생성
    }

    public void executeQuery() {
        Cursor cursor = database.rawQuery("select _id, title, content from noteData order by time DESC", null);
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
