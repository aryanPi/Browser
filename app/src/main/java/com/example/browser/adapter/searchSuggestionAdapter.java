package com.example.browser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.browser.activities.MainActivity;
import com.example.browser.R;
import com.example.browser.databinding.SearchSuggestionLayoutItemBinding;
import com.example.browser.fragment.SearchToolbarFrag;
import com.example.browser.model.BookmarksModel;
import com.example.browser.model.HistoryModel;
import com.example.browser.model.searchSuggestionItemModel;
import com.example.browser.utils.GlobalVariables;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import androidx.fragment.app.Fragment;

import static com.example.browser.fragment.SearchToolbarFrag.searchQueryHomePage;
import static com.example.browser.utils.GlobalVariables.getmActivity;

public class searchSuggestionAdapter {


    LinearLayout mLinearLayout;
    Context mContext;
    Realm realm = Realm.getDefaultInstance();
    Fragment mFragment;
    ArrayList<SearchSuggestionLayoutItemBinding> searchSuggestionLayoutItemBindings = new ArrayList<>();

    public searchSuggestionAdapter(Context context, LinearLayout linearLayout, Fragment fragment) {
        this.mLinearLayout = linearLayout;
        this.mContext = context;
        this.mFragment = fragment;
    }

    public void add(searchSuggestionItemModel model) {


        LayoutInflater vi = (LayoutInflater) getmActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SearchSuggestionLayoutItemBinding SearchSuggestionLayoutItem = SearchSuggestionLayoutItemBinding.inflate(vi);

        if (model.getFrom() == 0) {
            Glide.with(mContext).load(R.drawable.history_drawable).into(SearchSuggestionLayoutItem.getFromResultImage);
        } else if (model.getFrom() == 1) {
            Glide.with(mContext).load(R.drawable.bookmark_unselected_drawable).into(SearchSuggestionLayoutItem.getFromResultImage);
        } else if (model.getFrom() == 2) {
            Glide.with(mContext).load(R.drawable.search_drawable).into(SearchSuggestionLayoutItem.getFromResultImage);
        }
        SearchSuggestionLayoutItem.titleSearchSuggestion.setText(model.getTitle());
        SearchSuggestionLayoutItem.urlSearchSuggestion.setText(model.getUrl());


        SearchSuggestionLayoutItem.clickToLoadUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchToolbarFrag) mFragment).querySend(SearchSuggestionLayoutItem.urlSearchSuggestion.getText().toString());

            }
        });

        SearchSuggestionLayoutItem.openUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQueryHomePage.setText(SearchSuggestionLayoutItem.urlSearchSuggestion.getText());
                InputMethodManager keyboard = (InputMethodManager)
                        ((MainActivity) GlobalVariables.getmActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(searchQueryHomePage, 0);

            }
        });

        searchSuggestionLayoutItemBindings.add(SearchSuggestionLayoutItem);

        mLinearLayout.addView(SearchSuggestionLayoutItem.getRoot(), new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

    }


    public void removeAll() {
        mLinearLayout.removeAllViews();
    }


    public void update(String s) {
        realm.beginTransaction();

        ArrayList<searchSuggestionItemModel> searchSuggestionItemModelList = new ArrayList<>();

        RealmResults<HistoryModel> results = realm.where(HistoryModel.class).findAll();
        for (int j = 0; j < results.size(); j++) {
            if (results.get(j).getTitle().contains(s) || results.get(j).getUrl().contains(s)) {
                searchSuggestionItemModelList.add(new searchSuggestionItemModel(results.get(j).getTitle(), results.get(j).getUrl(), 0));
            }
        }


        RealmResults<BookmarksModel> resultsBookmark = realm.where(BookmarksModel.class).findAll();
        for (int k = 0; k < resultsBookmark.size(); k++) {
            if (resultsBookmark.get(k).getTitle().contains(s) || resultsBookmark.get(k).getUrl().contains(s)) {
                searchSuggestionItemModelList.add(new searchSuggestionItemModel(resultsBookmark.get(k).getTitle(), resultsBookmark.get(k).getUrl(), 1));
            }
        }


        realm.commitTransaction();

        for (int i = 0; i < searchSuggestionItemModelList.size(); i++) {
            if (!searchSuggestionItemModelList.get(i).getTitle().contains(s) && !searchSuggestionItemModelList.get(i).getUrl().contains(s)) {
                searchSuggestionItemModelList.remove(i);
            }
        }

        mLinearLayout.removeAllViews();


        for (int l = 0; l < searchSuggestionItemModelList.size(); l++) {
            boolean count = false;
            for (int m = 0; m < searchSuggestionItemModelList.size(); m++) {
                if (searchSuggestionItemModelList.get(m).getUrl().equals(searchSuggestionItemModelList.get(l).getUrl())) {
                    if (count){
                        searchSuggestionItemModelList.remove(l);
                        break;
                    }else{
                        count = true;
                    }
                }
            }
        }

        int in = 0;
        for (searchSuggestionItemModel temp : searchSuggestionItemModelList) {
            in++;
            add(temp);
            if (in > 9) break;
        }


    }

}
