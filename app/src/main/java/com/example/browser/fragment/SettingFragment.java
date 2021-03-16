package com.example.browser.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.browser.R;
import com.example.browser.activities.constants;
import com.example.browser.model.extra;
import com.example.browser.activities.selectLocation;
import com.example.browser.utils.GlobalVariables;
import com.example.browser.utils.MyApplication;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Locale;

import io.realm.Realm;

import static android.app.Activity.RESULT_OK;

public class SettingFragment extends Fragment {

    public static String TAG = "tomar";

    androidx.appcompat.widget.SwitchCompat switchCompatdownloadOnlyWifi;
    androidx.appcompat.widget.SwitchCompat switchCompatcompleteRing;
    androidx.appcompat.widget.SwitchCompat switchCompleteDownloadVibration;
    androidx.appcompat.widget.SwitchCompat switchDefaultBrowser;
    TextView downloadLocationDir;
    static View root;
    TextView nameOfSearchEngine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.setting_frag, container, false);

        RelativeLayout downloadOnlyWifi = root.findViewById(R.id.downloadonwifisettings);
        RelativeLayout downloadCompleteRing = root.findViewById(R.id.downloadcompleteringtonesettings);
        RelativeLayout downloadCompleteVibration = root.findViewById(R.id.downloadvibrationsettings);

        downloadLocationDir = root.findViewById(R.id.downloadLocationDir);
        downloadLocationDir.setText(GlobalVariables.getExtraData().getDownloadLocation());
        nameOfSearchEngine = root.findViewById(R.id.defaultSearhEngineName);

        switchCompatdownloadOnlyWifi = root.findViewById(R.id.downloadonlywifitoggle);
        switchCompatcompleteRing = root.findViewById(R.id.downloadringtonetoggle);
        switchCompleteDownloadVibration = root.findViewById(R.id.downloadvibrationsetting);
        switchDefaultBrowser = root.findViewById(R.id.defaultbrowsertogglesettings);


        extra temp = GlobalVariables.getExtraData();
        switchCompleteDownloadVibration.setChecked(temp.isDownloadCompleteVibration());
        switchCompatcompleteRing.setChecked(temp.isDownloadCompleteRingtone());
        switchDefaultBrowser.setChecked(temp.isDefaultBrowser());
        nameOfSearchEngine.setText(temp.getNameOfDefaultSearchEngine());


        root.findViewById(R.id.changeLanginesetting).setOnClickListener(v -> changeLanguage());

        root.findViewById(R.id.clearcachesettings).setOnClickListener(v -> deleteCache(getContext()));

        root.findViewById(R.id.defaultsearchenginesetting).setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.default_search_engine_popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView cancel = dialog.findViewById(R.id.cancel_btn_defaultbrowser);
            TextView select = dialog.findViewById(R.id.select_btn_defaultbrowser);
            cancel.setOnClickListener(v1 -> dialog.dismiss());
            select.setOnClickListener(v12 -> {
                RadioGroup r = dialog.findViewById(R.id.RadioBoxGroupDefault);
                String choose = "";
                RadioButton radio = dialog.findViewById(r.getCheckedRadioButtonId());
                String SearchEngine = radio.getText().toString();
                switch (r.getCheckedRadioButtonId()) {
                    case R.id.googleRadio:
                        choose = constants.googleQuery;
                        break;
                    case R.id.yahooRadio:
                        choose = constants.yahooQuery;
                        break;
                    case R.id.AskRadio:
                        choose = constants.askQuery;
                        break;
                    case R.id.bingRadio:
                        choose = constants.bingQuery;
                        break;
                    case R.id.DuckDuckGoRadio:
                        choose = constants.duckduckgoQuery;
                        break;
                    case R.id.YandexRadio:
                        choose = constants.yandexQuery;
                        break;
                    case R.id.aloRadio:
                        choose = constants.aloQuery;
                        break;
                    case R.id.NaverRadio:
                        choose = constants.NaverQuery;
                        break;

                    default:
                        break;
                }
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(extra.class).findFirst().setDefaultSearchEngine(choose);
                realm.where(extra.class).findFirst().setNameOfDefaultSearchEngine(SearchEngine);
                realm.commitTransaction();
                GlobalVariables.getExtraData().setDefaultSearchEngine(choose);
                nameOfSearchEngine.setText(SearchEngine);
                dialog.dismiss();
            });
            dialog.show();
        });


        switchCompatdownloadOnlyWifi.setOnCheckedChangeListener((buttonView, isChecked) -> Realm.getDefaultInstance().executeTransaction(realm -> realm.where(extra.class).findFirst().setDownloadOnlyWtithWifi(isChecked)));


        switchDefaultBrowser.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (temp.isDefaultBrowser()) {
//                Toast.makeText(getContext(), "Already Default Browser", Toast.LENGTH_SHORT).show();
                switchDefaultBrowser.setChecked(true);
            } else {

            }

            String url = "https://www.google.com";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setAction(Intent.ACTION_DEFAULT);
            intent.putExtra("default", "true");
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            startActivity(intent);


            /*PackageManager pm = GlobalVariables.getmActivity().getPackageManager();

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            GlobalVariables.getmActivity().startActivity(intent);
*/
            /*String userHomePackage = intent.resolveActivityInfo(pm, 0).packageName;
            String userHomeActivityClass = intent.resolveActivityInfo(pm, 0).name;
            String currentHomeLauncherName = intent.resolveActivityInfo(pm, 0).loadLabel(pm).toString();
*/
//            Log.d(TAG, "onCreateView: "+userHomeActivityClass+"\n"+userHomePackage+"\n"+currentHomeLauncherName);

            //Toast.makeText(getContext(), ""+str, Toast.LENGTH_SHORT).show();
            //    return str.equals(getPackageName());

        });

        root.findViewById(R.id.defaultbrowsersettings).setOnClickListener(v -> switchDefaultBrowser.setChecked(!switchDefaultBrowser.isChecked()));


        downloadCompleteVibration.setOnClickListener(v -> downloadCompleteVibration());


        downloadCompleteRing.setOnClickListener(v -> downloadCompleteRingFun());
        root.findViewById(R.id.downloadLocationSettings).setOnClickListener(v -> changeLocationDownload());


        return root;
    }

    private void changeLanguage() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.select_language_dialog_popup);
        dialog.findViewById(R.id.cancel_btn_lang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.select_btn_lang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectlanguage(((RadioGroup) dialog.findViewById(R.id.RadioBoxGroupLanguage)).getCheckedRadioButtonId());
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (deleteDir(dir)) {
                Toast.makeText(context, "Clean", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    private void downloadCompleteRingFun() {

        Realm realm = Realm.getInstance(MyApplication.config2);
        realm.beginTransaction();
        if (realm.where(extra.class).findFirst() != null) {
            if (switchCompatcompleteRing.isActivated()) {
                switchCompatcompleteRing.setChecked(false);
                realm.where(extra.class).findFirst().setDownloadCompleteRingtone(false);
            } else {
                switchCompatcompleteRing.setChecked(true);
                realm.where(extra.class).findFirst().setDownloadCompleteRingtone(true);
            }
        }
        realm.commitTransaction();
    }

    private void downloadCompleteVibration() {

        Realm realm = Realm.getInstance(MyApplication.config2);
        realm.beginTransaction();
        if (realm.where(extra.class).findFirst() != null) {
            if (switchCompleteDownloadVibration.isActivated()) {
                Toast.makeText(getContext(), "false", Toast.LENGTH_SHORT).show();
                switchCompleteDownloadVibration.setChecked(false);
                realm.where(extra.class).findFirst().setDownloadCompleteVibration(false);
            } else {
                Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
                switchCompleteDownloadVibration.setChecked(true);
                realm.where(extra.class).findFirst().setDownloadCompleteVibration(true);
            }
        }
        realm.commitTransaction();

    }


    public void changeLocationDownload() {
        Intent intent = new Intent(GlobalVariables.getmActivity(), selectLocation.class);
        startActivityForResult(intent, 0);


    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (!data.getStringExtra("downloadLocation").equals(""))
                    downloadLocationDir.setText(data.getStringExtra("downloadLocation"));
            }
        }
    }


    @SuppressLint("NonConstantResourceId")
    private void selectlanguage(int idOfSelected) {
        String localeLanguage = "";
        String localeContry = "";
        switch (idOfSelected) {
            case R.id.en:
                localeLanguage = "en";
                localeContry = "IN";
                break;
            case R.id.hi:
                localeLanguage = "hi";
                localeContry = "IN";
                break;
            case R.id.da:
                localeLanguage = "da";
                localeContry = "DE";
                break;
            case R.id.de:
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                localeLanguage = "de";
                localeContry = "DE";
                break;
            case R.id.fi:
                localeLanguage = "fi";
                localeContry = "AX";
                break;
            case R.id.el:
                localeLanguage = "el";
                localeContry = "GRC";
                break;
            case R.id.fr:
                localeLanguage = "fr";
                localeContry = "FR";
                break;
            case R.id.ga:
                localeLanguage = "ga";
                localeContry = "GA";
                break;
            default:
                break;
        }

        Resources resources = getResources();

        Locale locale = new Locale(localeLanguage, localeContry);


        /* Locale locale = new Locale("hi", "IN"); */


        Locale.setDefault(locale);
        android.content.res.Configuration config = new
                android.content.res.Configuration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        //restart base activity
        startActivity(getActivity().getIntent());
        getActivity().finish();

    }


}