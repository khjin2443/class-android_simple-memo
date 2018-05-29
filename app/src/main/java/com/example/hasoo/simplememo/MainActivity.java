package com.example.hasoo.simplememo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText, editText2;
    Button button5;
    int memoIndex = 1;
    Memo memo[] = new Memo[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // make instance
        Button button1 = (Button)findViewById(R.id.button);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button3 = (Button)findViewById(R.id.button3);
        Button button4 = (Button)findViewById(R.id.button4);
        button5 = (Button)findViewById(R.id.button5);

        editText = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);

        for(int i=0; i<3; i++){
            memo[i] = new Memo();
        }

        // onClick Listener for new memo
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                editText2.setText("");
            }
        });

        // onClick Listener for save memo
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memo[memoIndex-1].setMemo(
                        editText.getText().toString(),
                        editText2.getText().toString());
                String sToastMessage = "메모 " + memoIndex + "이 저장되었습니다.";
                Toast.makeText(getApplicationContext(), sToastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // onClick Listener for memo change
    void onMemoClick(View v){
        if(v.getId() == R.id.button){
            memoIndex = 1;
        } else if(v.getId() == R.id.button2){
            memoIndex = 2;
        } else if(v.getId() == R.id.button3){
            memoIndex = 3;
        } else {return;}

        String sMemoIndex = "[메모 " + memoIndex + "] 저장";
        editText.setText(memo[memoIndex-1].title);
        editText2.setText(memo[memoIndex-1].memo);
        button5.setText(sMemoIndex);
    }
}
