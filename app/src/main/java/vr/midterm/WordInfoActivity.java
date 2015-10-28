package vr.midterm;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class WordInfoActivity extends AppCompatActivity {
    private String word;
    SharedPreferences pref;
    ToggleButton bookMark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_info);
        word = getIntent().getStringExtra("Word");
        bookMark = (ToggleButton)findViewById(R.id.toggleButton);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String callValue = pref.getString(word, "null");
        if(callValue == "null"){
            bookMark.setChecked(false);
        }else{
            bookMark.setChecked(true);
        }
        bookMark.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor editor = pref.edit();
            @Override
            public void onClick(View v) {
                if (bookMark.isChecked()){
                    Log.i("WordInfo", "will be added");
                    editor.putString(word, word);
                    editor.commit();
                    Log.i("WordInfo", pref.getString(word, "null"));
                }else{
                    Log.i("WordInfo", "will be removed");
                    editor.remove(word);
                    editor.commit();
                }
            }
        });

    }

    private Cursor getInfo(){
        Cursor cur;
        cur = getContentResolver().query(WordInfoProvider.CONTENT_URI, null, " where upper(german)=upper('"+word+"');",
                null, null);
        return cur;
    }

    /**
     * When activity is onResume, then get the value from DB.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Cursor cur = getInfo();
        startManagingCursor(cur);
        cur.moveToNext();

        TextView textViewGerman = (TextView)findViewById(R.id.textView4);
        TextView textViewMeaning = (TextView)findViewById(R.id.textView6);
        TextView textViewSex = (TextView)findViewById(R.id.textView8);
        TextView textViewWordClass = (TextView)findViewById(R.id.textView10);

        textViewGerman.setText(cur.getString(1));
        textViewMeaning.setText(cur.getString(2));
        if(cur.getInt(4) != -1)
            textViewSex.setText(Gender.genderString[cur.getInt(4)]);
        else
            textViewSex.setText("-");
        textViewWordClass.setText(WordClass.wordclass[cur.getInt(3)]);
    }
}
