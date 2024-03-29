package vr.midterm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WordListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WordListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordListFragment extends ListFragment {
    OnWordSelectedListener mCallback;

    public interface OnWordSelectedListener{
        public void onWordSelected(String word);
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordListFragment newInstance(String param1, String param2) {
        WordListFragment fragment = new WordListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WordListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //((TextView)getActivity().findViewById(R.id.editText3)).setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_word_list, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnWordSelectedListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnWordSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.i("WordListFrag", "clicked");
        Cursor item = (Cursor)this.getListAdapter().getItem(position);
        String wordString;
        wordString = item.getString(item.getColumnIndex("german"));
        Log.i("WordListFrag", wordString);
        mCallback.onWordSelected(wordString);
        getListView().setItemChecked(position, true);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button searchButton = (Button)view.findViewById(R.id.button2);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String german = ((TextView) getActivity().findViewById(R.id.editText3)).getText().toString();
                Cursor cur = getActivity().getApplicationContext().getContentResolver().query(WordInfoProvider.CONTENT_URI,
                        null, " where upper(german)=upper('" + german + "')", null, null);
                getActivity().startManagingCursor(cur);
                ListAdapter adapt = new SimpleCursorAdapter(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        cur,
                        new String[]{"german"},
                        new int[]{android.R.id.text1}
                );
                setListAdapter(adapt);
            }
        });

        ListView listView = getListView();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String items[] = { "수정", "삭제" };
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setTitle("Select");
                Cursor item = (Cursor)parent.getItemAtPosition(position);
                final String wordString;
                wordString = item.getString(item.getColumnIndex("german"));
                ab.setSingleChoiceItems(items, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 각 리스트를 선택했을때

                            }
                        }).setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Log.d("Dialog", "onClick");
                                Intent intent;
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                switch(selectedPosition){
                                    case 0:
                                        intent = new Intent(getActivity().getApplicationContext(), FixWordActivity.class);
                                        intent.putExtra("Word",wordString);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        getActivity().getApplicationContext().getContentResolver().delete(WordInfoProvider.CONTENT_URI,
                                                "upper('"+wordString+"')", null);
                                        Cursor cur = getActivity().getApplicationContext().getContentResolver().query(WordInfoProvider.CONTENT_URI,
                                                null, " where upper(german)=upper('" + wordString + "')", null, null);
                                        getActivity().startManagingCursor(cur);
                                        ListAdapter adapt = new SimpleCursorAdapter(
                                                getContext(),
                                                android.R.layout.simple_list_item_1,
                                                cur,
                                                new String[]{"german"},
                                                new int[]{android.R.id.text1}
                                        );
                                        setListAdapter(adapt);
                                        TextView tv = (TextView)getActivity().findViewById(R.id.textView2);
                                        tv.setText("");
                                        Button button = (Button)getActivity().findViewById(R.id.button3);
                                        button.setVisibility(View.INVISIBLE);
                                        break;
                                }
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Cancel 버튼 클릭시
                            }
                        });
                ab.create();
                ab.show();

                return false;
            }
        });
    }



}
