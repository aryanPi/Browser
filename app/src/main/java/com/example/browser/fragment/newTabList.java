package com.example.browser.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.browser.activities.MainActivity;
import com.example.browser.R;
import com.example.browser.adapter.TabListAdapter;
import com.example.browser.utils.GlobalVariables;


public class newTabList extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public TabListAdapter tabListAdapter;

    public void close() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(THIS).commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    Fragment THIS;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_tab_list, container, false);

        ImageView blank =  root.findViewById(R.id.blank);
        THIS = this;
        ImageButton openNewTab = root.findViewById(R.id.newTabOpenBtn);
        ImageButton back = root.findViewById(R.id.back);
        ImageButton closeAll = root.findViewById(R.id.closeAllTab);


        ListView tablist = root.findViewById(R.id.tabList);

        tabListAdapter = new TabListAdapter(getContext(),getActivity(),R.layout.tab_list_item,this);
        tablist.setAdapter(tabListAdapter);


        closeAll.setOnClickListener(v -> {
            ((MainActivity)getActivity()).closeAllTab();
            tabListAdapter.notifyDataSetChanged();
        });

        openNewTab.setOnClickListener(v -> {
            ((MainActivity) getActivity()).newTabOpen();
            getActivity().getSupportFragmentManager().beginTransaction().remove(THIS).commit();
            tabListAdapter.notifyDataSetChanged();
                ((MainActivity) getActivity()).showAd();

        });


        blank.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().remove(THIS).commit();
        });


        back.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().remove(THIS).commit());

        return root;
    }
}