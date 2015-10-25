package vr.midterm;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

public class CheckOutWordActivity extends FragmentActivity implements WordListFragment.OnWordSelectedListener, WordFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_word);
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
