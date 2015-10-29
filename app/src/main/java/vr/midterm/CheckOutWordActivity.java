package vr.midterm;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CheckOutWordActivity extends FragmentActivity implements WordListFragment.OnWordSelectedListener, WordFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_word);

        Button activityButton = ((Button)(findViewById(R.id.button3)));
        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutWordActivity.this, WordInfoActivity.class);
                intent.putExtra("Word", ((TextView) (findViewById(R.id.editText3))).getText().toString());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onWordSelected(String word) {
        WordFragment defFrag = (WordFragment)getFragmentManager().findFragmentById(R.id.fragment2);
        defFrag.updateView(word);
    }

}
