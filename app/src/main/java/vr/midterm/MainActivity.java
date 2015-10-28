package vr.midterm;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView listView = (ListView) findViewById(android.R.id.list);
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MainMenuItems.menu);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent;
        switch(position){
            case 0:
                Log.i("ListActivity", "case addActivity");
                intent = new Intent(MainActivity.this, AddWordActivity.class);
                startActivity(intent);
                break;
            case 1:
                Log.i("ListActivity", "case checkout");
                intent = new Intent(MainActivity.this, CheckOutWordActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(MainActivity.this, PreferenceActivity.class);
                startActivity(intent);
                Log.i("ListActivity", "case 2");
                break;
        }
    }
}
