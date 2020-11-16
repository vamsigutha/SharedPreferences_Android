package com.example.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText message;
    SeekBar seekBar;
    float font_size;
    String font_color;
    String text_info;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = (EditText) findViewById(R.id.message);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);

        text_info = sharedPreferences.getString(getString(R.string.TEXT_INFO),"");
        message.setText(text_info);
        font_size = sharedPreferences.getFloat(getString(R.string.FONT_SIZE), 25);
        font_color = sharedPreferences.getString(getString(R.string.FONT_COLOR),"");

        message.setTextSize(TypedValue.COMPLEX_UNIT_PX,font_size);
        if(font_size==25){
            seekBar.setProgress(0);
        }
        else{
            seekBar.setProgress((int)font_size);
        }

        if(font_color.equals("RED")){
            message.setTextColor(Color.RED);
        }else if(font_color.equals("BLUE")) {
            message.setTextColor(Color.BLUE);
        }else if(font_color.equals("GREEN")){
            message.setTextColor(Color.GREEN);
        }



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                message.setTextSize(TypedValue.COMPLEX_UNIT_PX, progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                font_size = message.getTextSize();
            }
        });

    }

    public void changeColor(View view){
        switch(view.getId()){
            case R.id.red_color:
                message.setTextColor(Color.RED);
                font_color="Color.RED";
                break;
            case R.id.blue_color:
                message.setTextColor(Color.BLUE);
                font_color="BLUE";
                break;
            case R.id.green_color:
                message.setTextColor(Color.GREEN);
                font_color="GREEN";
                break;
        }
    }

    public void saveSettings(View view){
            SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(getString(R.string.FONT_SIZE),font_size);
            editor.putString(getString(R.string.FONT_COLOR),font_color);
            editor.putString(getString(R.string.TEXT_INFO),message.getText().toString());
            editor.commit();

            Toast.makeText(getApplicationContext(),"saved settings", Toast.LENGTH_LONG).show();

    }

    public void clearSettings(View view){
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        Toast.makeText(getApplicationContext(), "cleared settings", Toast.LENGTH_LONG).show();
    }

}