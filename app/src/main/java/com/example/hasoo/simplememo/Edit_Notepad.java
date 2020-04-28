package com.example.hasoo.simplememo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasoo.simplememo.DatabaseHelper;
import com.example.hasoo.simplememo.MainActivity;
import com.example.hasoo.simplememo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Edit_Notepad extends AppCompatActivity {
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    String tableName;
    String dbName;

    EditText editText_title;
    EditText editText_content;

    TextView textView_time;

    String title;
    String content;
    String time;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notepad);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        dbName = "notepad.db";
        tableName = "noteData";

        editText_title = findViewById(R.id.editText);
        editText_content = findViewById(R.id.editText2);

        Intent intent = getIntent();
//        인텐트 정보를 받음.
        title = intent.getExtras().getString("title");
        content = intent.getExtras().getString("content");
        position = intent.getExtras().getInt("position");
//        키 값

        editText_title.setText(title);
        editText_content.setText(content);

        Button button_save = findViewById(R.id.button2);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit_title = editText_title.getText().toString();
                String edit_content = editText_content.getText().toString();
                ContentValues contentValues = new ContentValues();

                if(TextUtils.isEmpty(edit_title) && TextUtils.isEmpty(edit_content)){
//                    제목과 내용이 모두 비어있으면 삭제함.
                    Toast.makeText(Edit_Notepad.this, R.string.Toast_not_save, Toast.LENGTH_LONG).show();
                    database.delete("noteData", "_id=?", new String[] {String.valueOf(position)});
//                    데이터베이스에서 삭제.
                    finish();
//                    액티비티 닫기
                    Intent intent1 = new Intent(Edit_Notepad.this, MainActivity.class);
                    startActivity(intent1);
//                    메인 액티비티 호출
                    return;
                }


//                불러온 값과 수정한 값이 같을경우 취소하고 메인액티비티 호출
                if (title.equals(edit_title) && content.equals(edit_content)) {
                    Intent intent1 = new Intent(Edit_Notepad.this, MainActivity.class);
                    startActivity(intent1);
                    return;
                }
                contentValues.put("title", edit_title);
                contentValues.put("content", edit_content);
                Toast.makeText(Edit_Notepad.this, R.string.Toast_edit, Toast.LENGTH_SHORT).show();
                database.update("noteData", contentValues, "_id=?", new String[] {String.valueOf(position)});
//                업데이트문 (수정)
                finish();
                Intent intent1 = new Intent(Edit_Notepad.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        Button button_delete = findViewById(R.id.button1);
//        삭제
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Edit_Notepad.this, R.string.Toast_delete, Toast.LENGTH_SHORT).show();
                database.delete("noteData", "_id=?", new String[] {String.valueOf(position)});
                finish();
                Intent intent1 = new Intent(Edit_Notepad.this, MainActivity.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Edit_Notepad.this, MainActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("position", position);
        startActivity(intent);
        finish();
//        백 버튼 터치하면 닫고 메인액티비티 띄움.
    }
}
