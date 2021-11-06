package com.waltonbd.smartscale.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.models.IndexItem;

import java.util.ArrayList;
import java.util.List;

public class IndexRepo {

    MutableLiveData<List<IndexItem>> mutableIndexList;

    public LiveData<List<IndexItem>> getIndexItems() {
        if (mutableIndexList == null) {
            mutableIndexList = new MutableLiveData<>();
            loadIndex();
        }
        return mutableIndexList;
    }

    private void loadIndex() {
        List<IndexItem> indexItems = new ArrayList<>();
        indexItems.add(new IndexItem("Weight", R.drawable.report_weight));
        indexItems.add(new IndexItem("Heart Rate", R.drawable.report_heart_rate));
        //indexItems.add(new IndexItem("Cardiac index", R.drawable.report_heart_index));
        indexItems.add(new IndexItem("BMI", R.drawable.report_bmi));
        indexItems.add(new IndexItem("Body Fat", R.drawable.report_bodyfat));
        indexItems.add(new IndexItem("Fat-free Body Weight", R.drawable.report_ffm));
        indexItems.add(new IndexItem("Subcutaneous Fat", R.drawable.report_subfat));
        indexItems.add(new IndexItem("Visceral Fat", R.drawable.report_visfat));
        indexItems.add(new IndexItem("Skeletal Muscle", R.drawable.report_skeletal_muscle));
        indexItems.add(new IndexItem("Muscle Mass", R.drawable.report_muscle_mass));
        indexItems.add(new IndexItem("Body Water", R.drawable.report_water));
        indexItems.add(new IndexItem("Bone Mass", R.drawable.report_bone));
        indexItems.add(new IndexItem("Protein", R.drawable.report_protein));
        indexItems.add(new IndexItem("BMR", R.drawable.report_bmr));
        indexItems.add(new IndexItem("Metabolic Age", R.drawable.report_bodyage));
        mutableIndexList.setValue(indexItems);
    }
}
