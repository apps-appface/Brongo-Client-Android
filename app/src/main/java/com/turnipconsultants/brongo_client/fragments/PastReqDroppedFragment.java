package com.turnipconsultants.brongo_client.fragments;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.PastReqAdapter;
import com.turnipconsultants.brongo_client.architecture.PastRequirementRepository;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.PastRequirementResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mohit on 12-02-2018.
 */

public class PastReqDroppedFragment extends Fragment {

    @BindView(R.id.noDataBTV)
    BrongoTextView noDataBTV;

    @BindView(R.id.pastReqRV)
    RecyclerView pastReqRV;

    private ArrayList<PastRequirementResponse.DataEntity> droppedArrayList;
    private PastReqAdapter pastReqAdapter;
    private Context context;
    private String headerDeviceId, headerPlatform, headerToken, mobileNo;
    private SharedPreferences prefs;
    private final String DEAL_TYPE = "OTHERS";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApiResponseModel().observeForever(modelObserver2);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_past_requirement_closed, container, false);
        ButterKnife.bind(this, v);
        initPage();
        PastRequirementRepository.LoadBrokersPastReq(context, headerDeviceId, headerPlatform, headerToken, mobileNo, DEAL_TYPE, getApiResponseModel());
        return v;
    }

    private void initPage() {
        context = getContext();
        prefs = context.getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE);
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = prefs.getString("token", "");
        mobileNo = prefs.getString(AppConstants.PREFS.USER_MOBILE_NO, "");
        droppedArrayList = new ArrayList<>();
        pastReqAdapter = new PastReqAdapter(context, droppedArrayList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        pastReqRV.setLayoutManager(manager);
        pastReqRV.setAdapter(pastReqAdapter);
    }

    private MutableLiveData<PastRequirementResponse> apiResponseModel;

    private MutableLiveData<PastRequirementResponse> getApiResponseModel() {
        if (apiResponseModel == null) {
            apiResponseModel = new MutableLiveData<>();
        }
        return apiResponseModel;
    }

    final Observer<PastRequirementResponse> modelObserver2 = new Observer<PastRequirementResponse>() {
        @Override
        public void onChanged(@Nullable final PastRequirementResponse newValue) {
            if (newValue != null) {
                AllUtils.LoaderUtils.dismissLoader();
                if (newValue.getStatusCode() == 200) {
                    List<PastRequirementResponse.DataEntity> dataEntityList = newValue.getData();
                    if (dataEntityList.size() == 0) {
                        pastReqRV.setVisibility(View.GONE);
                        noDataBTV.setVisibility(View.VISIBLE);
                    } else {
                        pastReqRV.setVisibility(View.VISIBLE);
                        noDataBTV.setVisibility(View.GONE);
                        droppedArrayList.addAll(dataEntityList);
                        pastReqAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(context, newValue.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                AllUtils.LoaderUtils.dismissLoader();
                Toast.makeText(context, "Please Try Again", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getApiResponseModel().removeObserver(modelObserver2);
    }
}
