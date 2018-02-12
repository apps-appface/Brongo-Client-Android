package com.turnipconsultants.brongo_client.fragments;

import android.app.Dialog;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.Listener.HelpFAQFragmentItemClickListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.FeedbackActivity;
import com.turnipconsultants.brongo_client.activities.SplashPagerActivity;
import com.turnipconsultants.brongo_client.activities.SplashScreenActivity;
import com.turnipconsultants.brongo_client.adapters.HelpFaqRecAdapter;
import com.turnipconsultants.brongo_client.architecture.HelpRepository;
import com.turnipconsultants.brongo_client.models.UnsubscribeModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.CommissionDialogFactory;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.RecyclerItemClickListener;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HelpSupportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.title)
    TextView toolBarTitle;

    @BindView(R.id.reset)
    TextView toolBarReset;

    @BindView(R.id.help_support_recycler_view)
    RecyclerView helSupport_RV;

    private ArrayList<String> arrayList;
    private Context context;
    private Unbinder unbinder;

    private HelpFAQFragmentItemClickListener mListener;
    private String headerToken, headerDeviceId, headerPlatform, mobileNo;
    private SharedPreferences pref;

    public HelpSupportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpSupportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpSupportFragment newInstance(String param1, String param2) {
        HelpSupportFragment fragment = new HelpSupportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context = getActivity();
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        mobileNo = pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "");
        getApiResponseModel().observeForever(modelObserver2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_help_support, container, false);

        unbinder = ButterKnife.bind(this, v);


        toolBarReset.setVisibility(View.GONE);
        toolBarTitle.setText("HELP & SUPPORT");
        getDummyList();
        helSupport_RV.setAdapter(new HelpFaqRecAdapter(context, arrayList, mListener));

        helSupport_RV.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    Intent i = new Intent(context, SplashPagerActivity.class);
                    i.putExtra("help", true);
                    startActivity(i);
                } else if (position == 1) {
                    UnsubscribeModel model = new UnsubscribeModel();
                    model.setMobileNo(mobileNo);
                    model.setMsg("");
                    model.setPlanType("");
                    showUnSubscribeDialog(model);
                }
            }
        }));
        return v;
    }

    private void showUnSubscribeDialog(final UnsubscribeModel model) {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.popup_dialog_two_btn);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView title = dialogBlock.findViewById(R.id.titletv);
        title.setVisibility(View.VISIBLE);
        title.setText("Alert");
        TextView message = dialogBlock.findViewById(R.id.thankyoutv);
        message.setText("Do you want to unsubscribe ?");
        Button yes = dialogBlock.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
                AllUtils.LoaderUtils.showLoader(context);
                HelpRepository.unsubscribe(context, headerDeviceId, headerPlatform, headerToken, mobileNo, model, getApiResponseModel());
            }
        });

        Button no = dialogBlock.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
            }
        });

        ImageView cancel = dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBlock.dismiss();
                    }
                });

        dialogBlock.show();
    }

    private void getDummyList() {
        arrayList = new ArrayList<>();
        arrayList.add("Take me to the app walkthrough");
        arrayList.add("I want to unsubscribe");
//        arrayList.add("I didn’t reveive the refer & earn’ amount");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HelpFAQFragmentItemClickListener) {
            mListener = (HelpFAQFragmentItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HelpSupportItemItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getApiResponseModel().removeObserver(modelObserver2);
    }

    private MutableLiveData<GeneralApiResponseModel> apiResponseModel;

    public MutableLiveData<GeneralApiResponseModel> getApiResponseModel() {
        if (apiResponseModel == null) {
            apiResponseModel = new MutableLiveData<>();
        }
        return apiResponseModel;
    }

    final Observer<GeneralApiResponseModel> modelObserver2 = new Observer<GeneralApiResponseModel>() {
        @Override
        public void onChanged(@Nullable final GeneralApiResponseModel newValue) {
            if (newValue != null) {
                AllUtils.LoaderUtils.dismissLoader();
                if (newValue.getStatusCode() == 200) {
                    showThankYouDialog(newValue.getMessage());
                } else {
                    Toast.makeText(context, newValue.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                AllUtils.LoaderUtils.dismissLoader();
                Toast.makeText(context, "Please Try Again", Toast.LENGTH_LONG).show();
            }
        }
    };

    private void showThankYouDialog(String messageApi) {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.thankyou_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView message = dialogBlock.findViewById(R.id.thankyoutv);
        message.setText(messageApi);
        Button back = dialogBlock.findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();

            }
        });

        ImageView cancel = dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBlock.dismiss();
                    }
                });

        dialogBlock.show();
    }

}
