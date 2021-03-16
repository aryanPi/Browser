package com.example.browser.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.browser.R;
import com.example.browser.activities.MainActivity;
import com.example.browser.utils.GlobalVariables;

import java.lang.reflect.Method;

public class SearchInPage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_in_page, container, false);

        WebView currentWebView = MainActivity.webViewArrayList.get(GlobalVariables.getActiveWebView());

        Fragment THIS = this;

        root.findViewById(R.id.closeSearchInPage).setOnClickListener(v -> {
            currentWebView.findAll("");
            try {
                Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                m.invoke(currentWebView, true);
            } catch (Exception ignored) {
            }
            onDestroyView();
            getActivity().getSupportFragmentManager().beginTransaction().remove(THIS).commit();
        });

        root.findViewById(R.id.upSearchInPage).setOnClickListener(v -> currentWebView.findNext(false));

        root.findViewById(R.id.downSearchPage).setOnClickListener(v -> currentWebView.findNext(true));


        TextView findBox = root.findViewById(R.id.searchTextInPage);

        findBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentWebView.findAll(s.toString());
                try {
                    Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                    m.invoke(currentWebView, true);
                } catch (Exception ignored) {
                }
            }
        });
        return root;
    }
}