package com.example.browser.fragment;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.browser.activities.History;
import com.example.browser.activities.MainActivity;
import com.example.browser.R;
import com.example.browser.adapter.searchSuggestionAdapter;
import com.example.browser.functions.PopupWindowCustom;
import com.example.browser.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CLIPBOARD_SERVICE;

public class SearchToolbarFrag extends Fragment {

    private static final int REQUEST_CODE_SPEAK = 314;
    Context mContext;

    Fragment THIS = this;

    @SuppressLint("StaticFieldLeak")
    public static EditText searchQueryHomePage;

    public static boolean copiedShow = false;

    public SearchToolbarFrag(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_toolbar, container, false);


        searchQueryHomePage = root.findViewById(R.id.searchQueryHomePage);
        LinearLayout searchSuggestionList = root.findViewById(R.id.searchSuggestionList);

        root.findViewById(R.id.doBack).setOnClickListener(v -> goBack());


        ImageView history = root.findViewById(R.id.historyOnSearchBar);

        history.setOnClickListener(v -> openHistory());

        ImageView back = root.findViewById(R.id.backSearchBar);

        back.setOnClickListener(v -> goBack());


        final searchSuggestionAdapter[] adapter = new searchSuggestionAdapter[1];
        searchQueryHomePage.setOnEditorActionListener((v, actionId, event) -> {
            querySend(v.getText().toString());
            return true;
        });

        searchQueryHomePage.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                if (!copiedShow) {
                    if (searchSuggestionList.getVisibility() == View.GONE) {
                        searchSuggestionList.setVisibility(View.VISIBLE);
                        adapter[0] = new searchSuggestionAdapter(GlobalVariables.getmActivity().getApplicationContext(), searchSuggestionList, SearchToolbarFrag.this);
                    }
                    if (mEdit.toString().equals("")) {
                        adapter[0].removeAll();

                    } else
                        adapter[0].update(mEdit.toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        root.findViewById(R.id.speak_image_search_home_able).setOnClickListener(v -> speak());


        searchQueryHomePage.requestFocus();
        searchQueryHomePage.postDelayed(() -> {
            InputMethodManager keyboard = (InputMethodManager)
                    GlobalVariables.getmActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(searchQueryHomePage, 0);

            if (!GlobalVariables.getSearchQToolbar().equals("")) {
                searchQueryHomePage.setText(GlobalVariables.getSearchQToolbar());
                searchQueryHomePage.selectAll();
                GlobalVariables.setSearchQToolbar("");
            }
        }, 300);

        if (GlobalVariables.isSearchCallFromHome()) {
            speak();
            GlobalVariables.setSearchCallFromHome(false);
        }

        return root;

    }

    private void openHistory() {
        Intent intent = new Intent(getActivity(), History.class);
        startActivity(intent);
    }

    private void goBack() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(THIS).commit();
        MainActivity.dragView.setVisibility(View.VISIBLE);
        ((MainActivity) GlobalVariables.getmActivity()).wraper.setVisibility(View.GONE);
        ((MainActivity) GlobalVariables.getmActivity()).homeFragment.setVisibility(View.VISIBLE);

    }


    public void querySend(String s) {
        ((MainActivity) GlobalVariables.getmActivity()).searQuery(s);
        goBack();
    }


    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Search !");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEAK);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEAK) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> query = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                searchQueryHomePage.setText(query.get(0));
                searchQueryHomePage.requestFocus();
                searchQueryHomePage.postDelayed(() -> {
                    // TODO Auto-generated method stub
                    InputMethodManager keyboard = (InputMethodManager)
                            GlobalVariables.getmActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(searchQueryHomePage, 0);
                }, 200);
            }
        }
    }

    @Override
    public void onDestroy() {
        searchQueryHomePage.setText("");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        searchQueryHomePage.setText("");
        super.onPause();
    }
}