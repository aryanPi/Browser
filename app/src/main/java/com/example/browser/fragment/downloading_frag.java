package com.example.browser.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.Constants;
import com.example.browser.R;
import com.example.browser.activities.Downloads;
import com.example.browser.activities.MainActivity;
import com.example.browser.activities.constants;
import com.example.browser.adapter.BottomSheetItemsAdapter;
import com.example.browser.databinding.DownloadRowBinding;
import com.example.browser.databinding.FragmentDownloadingFragBinding;
import com.example.browser.functions.extraFunctions;
import com.example.browser.model.DownloadModel;
import com.example.browser.threads.downloadService;
import com.example.browser.utils.GlobalVariables;
import com.example.browser.utils.MyApplication;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.Context.CLIPBOARD_SERVICE;


public class downloading_frag extends Fragment {


    @SuppressLint("StaticFieldLeak")

    static FragmentDownloadingFragBinding binding;
    static ArrayList<DownloadRowBinding> downloadRowBindingArrayList = new ArrayList<>();

    private static final String TAG = "DownloadActivity";

    static Realm realm = Realm.getInstance(MyApplication.config2);

    List<DownloadModel> downloadModelsLocal = new ArrayList<>();

    DownloadModel selectedDownloadModel;


    public static Fragment mFragment;

    @SuppressLint("StaticFieldLeak")
    static Activity activity;

    public downloading_frag(Activity activity) {
        downloading_frag.activity = activity;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDownloadingFragBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        Handler mHandler = new Handler(Looper.getMainLooper());

        GlobalVariables.setDownloadingFrag(this);

        downloadModelsLocal = realm.copyFromRealm(realm.where(DownloadModel.class).findAll());

        mFragment = this;

        downloadRowBindingArrayList.clear();

        new Thread(() -> {
            if (downloadModelsLocal != null) {
                if (downloadModelsLocal.size() > 0) {
                    for (int i = 0; i < downloadModelsLocal.size(); i++) {
                        addNewDownloadItem(downloadModelsLocal.get(i));
                    }
                }
            } else {
                Log.d(TAG, "onCreateView: null");
            }
            activity.runOnUiThread(() -> {
                if (binding.dataListDownloading.getChildCount() == 0) {
                    binding.downloadingText.setVisibility(View.GONE);
                    binding.dataListDownloading.setVisibility(View.GONE);
                }
                if (binding.dataListDownloaded.getChildCount() == 0) {
                    binding.dataListDownloaded.setVisibility(View.GONE);
                    binding.downloadedText.setVisibility(View.GONE);
                }
                if (binding.downloadedText.getVisibility() == View.GONE && binding.downloadingText.getVisibility() == View.GONE) {
                    // no downloads
                    binding.noDownloads.setVisibility(View.VISIBLE);
                } else {
                    binding.noDownloads.setVisibility(View.GONE);
                }

                GlobalVariables.setDownloadFragState(true);
            });
        }).start();

        return view;

    }

    private RealmResults<DownloadModel> getAllDownloads() {

        return realm.where(DownloadModel.class).findAll();
    }


    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void addNewDownloadItem(DownloadModel downloadModel) {
        LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DownloadRowBinding downloadRowBinding = DownloadRowBinding.inflate(vi);


        if (GlobalVariables.getDownloadingItem(downloadModel.getId()) == null) {
            GlobalVariables.setDownloadingItem(downloadModel.getId(), downloadModel.getId());
        } else {
            GlobalVariables.setDownloadingItem(downloadModel.getId(), GlobalVariables.getDownloadingItem(downloadModel.getId()) + 1);
        }

        downloadRowBinding.getRoot().setId(GlobalVariables.getDownloadingItem(downloadModel.getId()).intValue());

        activity.runOnUiThread(() -> {
            if (downloadModel.getUri().startsWith("draw")) {
                String uri = downloadModel.getUri();
                downloadRowBinding.imageLogoDownload.setBackground(activity.getDrawable(R.drawable.background_frame_layout_album_name));
                downloadRowBinding.imageLogoDownload.setPadding(15, 15, 15, 15);
                switch (uri) {
                    case constants.PDF_IMAGE:
                        Glide.with(activity).load(R.drawable.ic_pdf_image).fitCenter().into(downloadRowBinding.imageLogoDownload);
                        break;
                    case constants.IMG_IMAGE:
                        Glide.with(activity).load(R.drawable.ic_image).fitCenter().into(downloadRowBinding.imageLogoDownload);
                        break;
                    case constants.MUSICAL_IMAGE:
                        Glide.with(activity).load(R.drawable.ic_musical).fitCenter().into(downloadRowBinding.imageLogoDownload);
                        break;
                    case constants.ZIP_IMAGE:
                        Glide.with(activity).load(R.drawable.ic_zip).fitCenter().into(downloadRowBinding.imageLogoDownload);
                        break;
                    default:
                        Glide.with(activity).load(R.drawable.ic_file).fitCenter().into(downloadRowBinding.imageLogoDownload);
                        break;
                }
            } else if (!downloadModel.getUri().equals(""))
                Glide.with(activity).load(extraFunctions.convert(downloadModel.getUri())).centerCrop().into(downloadRowBinding.imageLogoDownload);
            else {
                downloadRowBinding.imageLogoDownload.setBackground(activity.getDrawable(R.drawable.background_frame_layout_album_name));
                downloadRowBinding.imageLogoDownload.setPadding(15, 15, 15, 15);
                Glide.with(activity).load(R.drawable.ic_file).fitCenter().into(downloadRowBinding.imageLogoDownload);
            }
        });


        downloadRowBinding.menuDownloadedItem.setVisibility(View.GONE);
        downloadRowBinding.fileTitle.setText(downloadModel.getTitle());

        downloadRowBinding.fileSizeTotal.setText(android.text.format.Formatter.formatFileSize(activity, downloadModel.getTotal_file_Size()));
        downloadRowBinding.fileSize.setText(android.text.format.Formatter.formatFileSize(activity, downloadModel.getFile_size()));

        if (downloadModel.getStatus() == Constants.DOWNLOAD_STATUS.COMPLETE) {
            downloadRowBinding.mainRel.setOnClickListener(v -> ((downloading_frag) mFragment).clickedDownloadedItem(v, downloadModel.getId(), downloadRowBinding.checkBoxDownloads));

            downloadRowBinding.fileProgress.setVisibility(View.GONE);
            downloadRowBinding.pauseDownloadingBtn.setVisibility(View.GONE);
            downloadRowBinding.fileSize.setVisibility(View.GONE);
            downloadRowBinding.downloadSpeedText.setVisibility(View.GONE);
            downloadRowBinding.menuDownloadedItem.setVisibility(View.VISIBLE);
            downloadRowBinding.menuDownloadedItem.setOnClickListener(new View.OnClickListener() {
                final Long id = downloadModel.getId();

                @Override
                public void onClick(View v) {
                    openMenu(downloadModel, 1);

                }
            });
        } else if (downloadModel.getStatus() == Constants.DOWNLOAD_STATUS.RUNNING || downloadModel.getStatus() == Constants.DOWNLOAD_STATUS.PAUSE) {

            downloadRowBinding.mainRel.setOnClickListener(v -> ((downloading_frag) mFragment).clickedDownloadedItem(v, downloadModel.getId(), downloadRowBinding.checkBoxDownloads));

            downloadRowBinding.fileProgress.setProgress(downloadModel.getProgress());
            /*downloadRowBinding.cancelDownloadingBtn.setOnClickListener(v -> {
                selected = downloadModel.getId();
                ((downloading_frag) mFragment).removeItem();

            });*/

            downloadRowBinding.fileSizeTotal.setText(android.text.format.Formatter.formatFileSize(activity, downloadModel.getTotal_file_Size()));
            downloadRowBinding.fileSize.setText(android.text.format.Formatter.formatFileSize(activity, downloadModel.getFile_size()) + "/");

            if (downloadModel.getStatus() == Constants.DOWNLOAD_STATUS.PAUSE) {
                downloadRowBinding.downloadSpeedText.setText("PAUSED");
                downloadRowBinding.pauseDownloadingBtn.setImageDrawable(GlobalVariables.getResources().getDrawable(R.drawable.resume_download_drawable));
            }

            downloadRowBinding.pauseDownloadingBtn.setOnClickListener(v -> {
                if (downloadModel.getStatus() == Constants.DOWNLOAD_STATUS.PAUSE) {

                    ((MainActivity) GlobalVariables.getmActivity()).startDownload(downloadModel, true);

                    downloadRowBinding.pauseDownloadingBtn.setImageDrawable(GlobalVariables.getResources().getDrawable(R.drawable.pause_download_drawable));
                } else {
                    downloadService.pauseDownload(downloadModel.getId());
                    downloadRowBinding.pauseDownloadingBtn.setImageDrawable(GlobalVariables.getResources().getDrawable(R.drawable.resume_download_drawable));
                }
                realm.executeTransaction(realm -> downloadModel.setStatus(downloadModel.getStatus() == Constants.DOWNLOAD_STATUS.PAUSE ? Constants.DOWNLOAD_STATUS.RUNNING : Constants.DOWNLOAD_STATUS.PAUSE));
            });

        } else if (downloadModel.getStatus() == Constants.DOWNLOAD_STATUS.FAILED) {
            downloadRowBinding.fileProgress.setVisibility(View.GONE);
            downloadRowBinding.downloadSpeedText.setTextColor(Color.parseColor("#FFFF0000"));
            downloadRowBinding.downloadSpeedText.setText("Failed Downloaded !");
            downloadRowBinding.fileSize.setVisibility(View.GONE);
            downloadRowBinding.fileSizeTotal.setVisibility(View.GONE);
            downloadRowBinding.pauseDownloadingBtn.setVisibility(View.GONE);
            downloadRowBinding.menuDownloadedItem.setVisibility(View.VISIBLE);
            downloadRowBinding.menuDownloadedItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMenu(downloadModel, 0);
                }
            });
        }

        if (!downloadModel.isDownloadingWith()) {
            if (downloadModel.getStatus() != Constants.DOWNLOAD_STATUS.COMPLETE) {
                downloadRowBinding.fileProgress.setVisibility(View.VISIBLE);
            } else
                downloadRowBinding.menuDownloadedItem.setVisibility(View.VISIBLE);
            downloadRowBinding.downloadSpeedText.setText("Downloading With Download Manager !");
            downloadRowBinding.pauseDownloadingBtn.setVisibility(View.GONE);
        }

        activity.runOnUiThread(() -> {
            downloadRowBindingArrayList.add(downloadRowBinding);
            if (downloadModel.getStatus() == Constants.DOWNLOAD_STATUS.COMPLETE) {
                binding.dataListDownloaded.addView(downloadRowBinding.getRoot(), new LinearLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            } else {
                binding.dataListDownloading.addView(downloadRowBinding.getRoot(), new LinearLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            }
        });
    }

    BottomSheetDialog bottomSheetDialog;

    private void openMenu(DownloadModel model, int flag) {
        bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        selectedDownloadModel = model;
        View sheetView = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.bottomsheet_items_custom, (LinearLayout) activity.findViewById(R.id.bottomSheetDownloadFailed));
        ((TextView) sheetView.findViewById(R.id.titleOfFileBottomsheetDownloadFailed)).setText(model.getTitle());
        ((ListView) sheetView.findViewById(R.id.list_item_bottomSheet)).setAdapter(new BottomSheetItemsAdapter(getContext(), R.layout.bottomsheet_items_custom, createArrayList(flag), this, 0));
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private ArrayList<String> createArrayList(int flag) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (flag == 0) {
            arrayList.add("Retry Download_ret");
            arrayList.add("Download With Download Manager_dwdm");
        } else if (flag == 1) {
            arrayList.add("Share_sh");
            arrayList.add("Rename_re");
            arrayList.add("Location_lo");
        }
        arrayList.add("Delete_de");
        arrayList.add("Remove From Download_rem");
        return arrayList;
    }


    public void click(String toString) {
        switch (toString) {
            case "Share":
                extraFunctions.shareFile(selectedDownloadModel.getFile_path() + selectedDownloadModel.getTitle());
                break;
            case "Delete":
                ((Downloads) getActivity()).deleteItem(selectedDownloadModel.getFile_path() + selectedDownloadModel.getTitle(), this, selectedDownloadModel.getStatus() == Constants.DOWNLOAD_STATUS.COMPLETE ? selectedDownloadModel.getId() : -1L);
                break;
            case "Location":
                ((Downloads) getActivity()).location(selectedDownloadModel.getFile_path() + selectedDownloadModel.getTitle(), getContext());
                break;
            case "Retry Download":
                ((MainActivity) GlobalVariables.getmActivity()).startDownload(selectedDownloadModel, false);
                break;
            case "Rename":
                ((Downloads) getContext()).rename(this, selectedDownloadModel.getFile_path(), selectedDownloadModel.getTitle(), selectedDownloadModel.getId());
                break;
            case "Download With Download Manager":
                extraFunctions.downloadWithDownloadManager(selectedDownloadModel);
                break;
            case "Remove From Download":
                removeItem(selectedDownloadModel.getId());
                break;
            default:
                break;
        }
        bottomSheetDialog.dismiss();
    }

    public void popupDownloadingItemMenu(View v) {

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void selectionModeDownloadsSetFalse() {
        isSelectionModeDownloads = false;
        for (int i = 0; i < downloadRowBindingArrayList.size(); i++) {
            downloadRowBindingArrayList.get(i).mainRel.setBackground(mFragment.getResources().getDrawable(R.drawable.transparent));
            downloadRowBindingArrayList.get(i).checkBoxDownloads.setChecked(false);
            downloadRowBindingArrayList.get(i).checkBoxDownloads.setVisibility(View.GONE);
            //       selectedItems.clear();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void clickedDownloadedItem(View v, Long id, CheckBox checkBok) {
        if (isSelectionModeDownloads)
            checkBok.performClick();
        else
            openFile(id);

    }

    private void openFile(Long id) {

    }

    public int selectedItems = 0;
    public boolean isSelectionModeDownloads = false;

    public void selectionMode(boolean b) {
        if (b)
            for (int i = 0; i < downloadRowBindingArrayList.size(); i++) {
                downloadRowBindingArrayList.get(i).checkBoxDownloads.setVisibility(View.VISIBLE);
                int finalI = i;
                downloadRowBindingArrayList.get(i).checkBoxDownloads.setOnClickListener(null);
                downloadRowBindingArrayList.get(i).checkBoxDownloads.setOnClickListener(v -> {
                    if (downloadRowBindingArrayList.get(finalI).checkBoxDownloads.isChecked()) {
                        selectedItems++;
                        downloadRowBindingArrayList.get(finalI).mainRel.setBackground(getResources().getDrawable(R.drawable.selected_item_bg));
                    } else {
                        selectedItems--;
                        downloadRowBindingArrayList.get(finalI).mainRel.setBackground(getResources().getDrawable(R.drawable.transparent));
                    }
                    ((Downloads) getActivity()).checkAndUpdateCheckbox(true, selectedItems);
                });
            }
        else {
            for (int i = 0; i < downloadRowBindingArrayList.size(); i++) {
                downloadRowBindingArrayList.get(i).checkBoxDownloads.setChecked(false);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void updateView(Long id, Long size, Long totalSize, boolean downloadWith) {
        Long i2 = GlobalVariables.getDownloadingItem(id);
        Log.d(TAG, "updateView: " + i2);
        for (int i = 0; i < downloadRowBindingArrayList.size(); i++) {
            if ((downloadRowBindingArrayList.get(i).getRoot().getId()) == i2) {
                if (downloadWith) {
                    downloadRowBindingArrayList.get(i).fileProgress.setProgress(totalSize.intValue());
                    downloadRowBindingArrayList.get(i).fileSize.setText(android.text.format.Formatter.formatFileSize(activity, size) + "/");
                } else {
                    if (totalSize != 0) {
                        int progressLocal = (int) ((size * 100L) / Long.parseLong(String.valueOf(totalSize)));
                        downloadRowBindingArrayList.get(i).fileProgress.setProgress(progressLocal);
                        downloadRowBindingArrayList.get(i).fileSize.setText(android.text.format.Formatter.formatFileSize(activity, size) + "/");

                    } else {
                        Log.d("TAGDDD", "downloadprocessInit: 2" + "   " + totalSize);
                    }
                }
                break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void updateSpeed(Long id, String speed) {
        Long i2 = GlobalVariables.getDownloadingItem(id);
        for (int i = 0; i < downloadRowBindingArrayList.size(); i++) {
            if ((downloadRowBindingArrayList.get(i).getRoot().getId()) == i2) {
                downloadRowBindingArrayList.get(i).downloadSpeedText.setText(speed + "/s");
                break;
            }
        }
    }

    public void updateTotalFileSize(Long id, Long totalSize) {
        Long i2 = GlobalVariables.getDownloadingItem(id);
        for (int i = 0; i < downloadRowBindingArrayList.size(); i++) {
            if ((downloadRowBindingArrayList.get(i).getRoot().getId()) == i2) {
                downloadRowBindingArrayList.get(i).fileSizeTotal.setText(android.text.format.Formatter.formatFileSize(activity, totalSize));
                break;
            }
        }
    }

    public void failedDownload(Long downloadId) {
        Long i2 = GlobalVariables.getDownloadingItem(downloadId);
        for (int i = 0; i < downloadRowBindingArrayList.size(); i++) {
            if ((downloadRowBindingArrayList.get(i).getRoot().getId()) == i2) {
                DownloadRowBinding temp = downloadRowBindingArrayList.get(i);
                temp.fileProgress.setVisibility(View.GONE);
                temp.downloadSpeedText.setTextColor(Color.parseColor("#FFFFFF"));
                temp.downloadSpeedText.setText("Failed Downloaded !");
                temp.fileSize.setVisibility(View.GONE);
                temp.fileSizeTotal.setVisibility(View.GONE);
                temp.notnullTemp.setVisibility(View.GONE);
                break;
            }
        }
    }


    public void finishDownload(Long id) {
        Long i2 = GlobalVariables.getDownloadingItem(id);
        for (int i = 0; i < downloadRowBindingArrayList.size(); i++) {
            if ((downloadRowBindingArrayList.get(i).getRoot().getId()) == i2) {
                DownloadRowBinding temp = downloadRowBindingArrayList.get(i);
                temp.fileProgress.setVisibility(View.GONE);
                temp.fileSize.setVisibility(View.GONE);
                temp.fileSizeTotal.setVisibility(View.VISIBLE);
                temp.downloadSpeedText.setVisibility(View.GONE);
                temp.pauseDownloadingBtn.setVisibility(View.GONE);
                temp.menuDownloadedItem.setVisibility(View.VISIBLE);
                temp.menuDownloadedItem.setOnClickListener(v -> {
                    for (DownloadModel temp2 : downloadModelsLocal) {
                        if (temp2.getId().equals(id)) {
                            ((downloading_frag) mFragment).openMenu(temp2, 1);
                        }
                    }
                });
                break;
            }
        }
    }


    public void rename(String Newname, Long idSelected) {
        Long id2 = GlobalVariables.getDownloadingItem(idSelected);
        for (int i = 0; i < downloadRowBindingArrayList.size(); i++) {
            if (downloadRowBindingArrayList.get(i).getRoot().getId() == id2) {
                downloadRowBindingArrayList.get(i).fileTitle.setText(Newname);
                break;
            }
        }
        realm.executeTransaction(realm -> realm.where(DownloadModel.class).equalTo("id", idSelected).findFirst().setTitle(Newname));
    }


    public void removeItem(Long idSelected) {
        realm.beginTransaction();
        downloadService.cancelDownload(idSelected);
        DownloadModel temp = realm.where(DownloadModel.class).equalTo("id", idSelected).findFirst();
        Long id2 = GlobalVariables.getDownloadingItem(idSelected);
        for (int i = 0; i < downloadRowBindingArrayList.size(); i++) {
            if (downloadRowBindingArrayList.get(i).getRoot().getId() == id2) {
                if (temp.getStatus() == Constants.DOWNLOAD_STATUS.COMPLETE) {
                    binding.dataListDownloaded.removeView(downloadRowBindingArrayList.get(i).getRoot());
                } else {
                    binding.dataListDownloading.removeView(downloadRowBindingArrayList.get(i).getRoot());
                }
                break;
            }
        }
        temp.deleteFromRealm();
        realm.commitTransaction();
    }

    @Override
    public void onDestroyView() {
        GlobalVariables.setDownloadFragState(false);
        super.onDestroyView();
        binding = null;
    }


    public void selectAll(boolean isChecked) {
        Toast.makeText(activity, "Here"+isChecked, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < downloadRowBindingArrayList.size(); i++) {
            if (isChecked) {
                if (!downloadRowBindingArrayList.get(i).checkBoxDownloads.isChecked())
                    downloadRowBindingArrayList.get(i).checkBoxDownloads.performClick();
            } else {
                if (downloadRowBindingArrayList.get(i).checkBoxDownloads.isChecked())
                    downloadRowBindingArrayList.get(i).checkBoxDownloads.performClick();
            }
        }
    }


    public void deletePerformSelected() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.delete_downloaded_item);
        ((TextView) dialog.findViewById(R.id.headlineDeleteDialigeItem)).setText("Are you sure to remove Selected Items ?");
        dialog.findViewById(R.id.delete_btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Long, Long> temp = GlobalVariables.getDownloadingItemHashMap();
                for (int i = 0; i < downloadRowBindingArrayList.size(); i++) {
                    if (downloadRowBindingArrayList.get(i).checkBoxDownloads.isChecked()) {
                        for (Map.Entry<Long, Long> set :
                                temp.entrySet()) {
                            if (set.getValue() == downloadRowBindingArrayList.get(i).getRoot().getId()) {
                                removeItem(set.getKey());
                            }
                        }

                    }
                }
                ((Downloads)getActivity()).checkAndUpdateCheckbox(false,0);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.cancel_btn_delete).setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

/*
    public int getSelectedCount() {
        int count = 0;
        for (int i = 0; i < downloadRowBindingArrayList.size(); i++)
            if (downloadRowBindingArrayList.get(i).checkBoxDownloads.isChecked()) count++;
        return count;
    }*/
}