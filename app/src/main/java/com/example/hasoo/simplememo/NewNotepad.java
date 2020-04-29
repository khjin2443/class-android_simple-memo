package com.example.hasoo.simplememo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewNotepad extends AppCompatActivity {
    DatabaseHelper dbHelper;
    EditText editText_title;
    EditText editText_content ;
    SQLiteDatabase database;
    String tableName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notepad);

        tableName = "noteData";
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        editText_title = findViewById(R.id.new_editText);
        editText_content = findViewById(R.id.new_editText2);

        Button button4 = findViewById(R.id.button4);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               editText_title.setText("");
               editText_content.setText("");
            }
        });

        editText_content.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        Button button = findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteAdapter adapter = new NoteAdapter();
                adapter.addItem(new NoteList(editText_title.getText().toString(), editText_content.getText().toString()));
//                어댑터에 아이템을 추가.

                String input_title = editText_title.getText().toString();
                String input_content = editText_content.getText().toString();

//                저장
                try {
                    ContentValues contentValues = new ContentValues();

//                    입력된 내용이 아무것도 없을경우 취소.
                    if (TextUtils.isEmpty(input_title) && TextUtils.isEmpty(input_content)) {
                        Toast.makeText(NewNotepad.this, R.string.Toast_not_save, Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(NewNotepad.this, MainActivity.class);
                        startActivity(intent);
                        return;
                    }

                    contentValues.put("title", input_title);
                    contentValues.put("content", input_content);
                    Toast.makeText(NewNotepad.this, R.string.Toast_save, Toast.LENGTH_SHORT).show();
//                    저장
                    database.insert(tableName, null, contentValues);
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                }

                finish();
                Intent intent = new Intent(NewNotepad.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(NewNotepad.this, MainActivity.class);
        startActivity(intent);
//        뒤로가기 버튼 터치시 닫고 메인액티비티를 새로 띄움
    }
}
