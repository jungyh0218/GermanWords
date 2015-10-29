package vr.midterm;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AddWordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        ArrayList<String> menuList = new ArrayList<String>();
menuList.add("명사");
        menuList.add("동사");
        menuList.add("형용사");
        menuList.add("부사");
        menuList.add("기타");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_dropdown_item, menuList);
        //스피너 속성
        Spinner sp = (Spinner) this.findViewById(R.id.spinner);
        //sp.setPrompt("품사 선택"); // 스피너 제목
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
        }

@Override
public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //when the selection is 'noun', then set sex radiobutton visible.
        TextView sexText = (TextView)findViewById(R.id.textView);
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        if(position == 0){
        sexText.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
        ((RadioButton)findViewById(R.id.radioButton)).setSelected(true);
        }else{
        sexText.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.INVISIBLE);
        }
        }

@Override
public void onNothingSelected(AdapterView<?> parent) {

        }

public void onClickInsert(View v){
        String german = ((TextView)findViewById(R.id.editText)).getText().toString();
        String korean = ((TextView)findViewById(R.id.editText2)).getText().toString();
        int wordclass = ((Spinner)findViewById(R.id.spinner)).getSelectedItemPosition();
        int gender = -1;

        RadioGroup group = (RadioGroup)findViewById(R.id.radioGroup);
        if(group.getCheckedRadioButtonId() != -1) {

        switch (group.getCheckedRadioButtonId()) {
        case R.id.radioButton:
        gender = 0;
        break;
        case R.id.radioButton2:
        gender = 1;
        break;
        case R.id.radioButton3:
        gender = 2;
        break;
        }
        }

        ContentValues values = new ContentValues();
        values.put("german", german);
        values.put("korean", korean);
        values.put("wordclass", wordclass);
        values.put("gender", gender);

        getContentResolver().insert(WordInfoProvider.CONTENT_URI, values);
        finish();
        }
        }
