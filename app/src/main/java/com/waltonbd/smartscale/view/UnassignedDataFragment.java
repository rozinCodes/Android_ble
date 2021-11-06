package com.waltonbd.smartscale.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.waltonbd.smartscale.R;
import com.waltonbd.smartscale.adapters.UnassignedListDataAdapter;
import com.waltonbd.smartscale.api.APIClient;
import com.waltonbd.smartscale.api.BaseResponse;
import com.waltonbd.smartscale.api.UnassignedRequest;
import com.waltonbd.smartscale.constant.ConstantValues;
import com.waltonbd.smartscale.databinding.FragmentUnassignedDataBinding;
import com.waltonbd.smartscale.interfaces.OnFragmentCallback;
import com.waltonbd.smartscale.models.unassigned_model.UnassignedData;
import com.waltonbd.smartscale.models.unassigned_model.UnassignedDetails;
import com.waltonbd.smartscale.util.ToastMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnassignedDataFragment extends Fragment {

    private FragmentUnassignedDataBinding binding;

    private final List<UnassignedData> dataList = new ArrayList<>();
    private UnassignedListDataAdapter adapter;

    private KProgressHUD progressDialog;

    public static UnassignedDataFragment newInstance() {
        return new UnassignedDataFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OnFragmentCallback) requireContext()).onCallback(getString(R.string.title_unassigned));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUnassignedDataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init progress dialog
        progressDialog = KProgressHUD.create(requireContext())
                .setLabel("Please Wait...")
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

        binding.dataLoader.setVisibility(View.GONE);
        binding.noRecordTv.setVisibility(View.GONE);
        binding.unassignedRly.setVisibility(View.GONE);

        adapter = new UnassignedListDataAdapter(dataList, requireContext());
        binding.unassignedRv.setAdapter(adapter);

        getUnassignedData();

        adapter.setOnItemSelectListener((itemsList, isChecked) -> {
            binding.unassignedRly.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        binding.assignBtn.setOnClickListener(view1 -> {
            List<Integer> itemsId = adapter.getCheckedItemsId();
            assignData(itemsId);
        });

        binding.deleteBtn.setOnClickListener(view1 -> {
            List<Integer> itemsId = adapter.getCheckedItemsId();
            deleteData(itemsId);
        });
    }

    private void assignData(List<Integer> itemsId) {

    }

    private void getUnassignedData() {
        binding.dataLoader.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = APIClient.getInstance().getUnassignedData(ConstantValues.ACCESS_TOKEN, ConstantValues.USER_ID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.dataLoader.setVisibility(View.GONE);

                if (response.body() != null) {
                    try {
                        dataList.clear();

                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Iterator<String> key = jsonObject.keys();
                        while (key.hasNext()) {
                            List<UnassignedDetails> detailsList = new ArrayList<>();

                            String keyDate = key.next();
                            JSONArray array = jsonObject.getJSONArray(keyDate);
                            for (int i = 0; i < array.length(); i++) {
                                UnassignedDetails details = new Gson().fromJson(array.getString(i), UnassignedDetails.class);
                                detailsList.add(details);
                            }

                            UnassignedData data = new UnassignedData();
                            data.setDate(keyDate);
                            data.setDetails(detailsList);
                            dataList.add(data);
                        } // while loop end

                        // setting groupArrayList adapter
                        adapter.notifyDataSetChanged();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    binding.noRecordTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.dataLoader.setVisibility(View.GONE);
            }
        });
    }

    private void deleteData(List<Integer> itemsId) {
        progressDialog.show();

        UnassignedRequest request = new UnassignedRequest(itemsId);
        APIClient.getInstance().deleteUnassignedData(ConstantValues.ACCESS_TOKEN, request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    ToastMaker.show(requireContext(), response.body().getMessage());
                    if (response.body().getStatus()) {
                        getUnassignedData();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}