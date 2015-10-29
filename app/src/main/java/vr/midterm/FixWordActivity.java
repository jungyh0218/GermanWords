package vr.midterm;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class FixWordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String wordString;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_word);
        Intent intent = getIntent();
        wordString = intent.getStringExtra("Word");

        ArrayList<String> menuList = new ArrayList<String>();
        menuList.add("명사");
        menuList.add("동사");
        menuList.add("형용사");
        menuList.add("부사");
        menuList.add("기타");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, menuList);
        //스피너 속성
        Spinner sp = (Spinner) this.findViewById(R.id.fixspinner);
        //sp.setPrompt("품사 선택"); // 스피너 제목
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
        init();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //when the selection is 'noun', then set sex radiobutton visible.
        TextView sexText = (TextView)findViewById(R.id.fixtextView);
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.fixradioGroup);
        if(position == 0){
            sexText.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.fixradioButton)).setSelected(true);
        }else{
            sexText.setVisibility(View.INVISIBLE);
            radioGroup.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onClickInsert(View v){
        String german = ((TextView)findViewById(R.id.germanText)).getText().toString();
        String korean = ((TextView)findViewById(R.id.meaningText)).getText().toString();
        int wordclass = ((Spinner)findViewById(R.id.fixspinner)).getSelectedItemPosition();
        int gender = -1;

        RadioGroup group = (RadioGroup)findViewById(R.id.fixradioGroup);
        if(group.getCheckedRadioButtonId() != -1) {

            switch (group.getCheckedRadioButtonId()) {
                case R.id.fixradioButton:
                    gender = 0;
                    break;
                case R.id.fixradioButton2:
                    gender = 1;
                    break;
                case R.id.fixradioButton3:
                    gender = 2;
                    break;
            }
        }

        ContentValues values = new ContentValues();
        values.put("german", german);
        values.put("korean", korean);
        values.put("wordclass", wordclass);
        values.put("gender", gender);
        getContentResolver().update(WordInfoProvider.CONTENT_URI, values, Integer.toString(id), null);
        finish();
    }

    private void init(){
        Cursor cur = getContentResolver().query(WordInfoProvider.CONTENT_URI, null,
                " where upper(german)=upper('"+wordString+"');", null, null);
        cur.moveToNext();
        id = cur.getInt(0);
        EditText german = (EditText)findViewById(R.id.germanText);
        german.setText(cur.getString(1));
        EditText meaning = (EditText)findViewById(R.id.meaningText);
        meaning.setText(cur.getString(2));
        //gender
        RadioGroup radio = (RadioGroup)findViewById(R.id.fixradioGroup);
        switch(cur.getInt(4)){
            case 0:
                radio.check(R.id.fixradioButton);
                break;
            case 1:
                radio.check(R.id.fixradioButton2);
                break;
            case 2:
                radio.check(R.id.fixradioButton3);
                break;
        }
        Spinner spinner = (Spinner)findViewById(R.id.fixspinner);
        spinner.setSelection(cur.getInt(3));
    }
}
