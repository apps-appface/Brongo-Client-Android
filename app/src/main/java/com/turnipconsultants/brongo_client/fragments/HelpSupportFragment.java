package com.turnipconsultants.brongo_client.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turnipconsultants.brongo_client.Listener.HelpFAQFragmentItemClickListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.HelpFaqRecAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_help_support, container, false);

        unbinder = ButterKnife.bind(this, v);
        context = getActivity();

        toolBarReset.setVisibility(View.GONE);
        toolBarTitle.setText("HELP & SUPPORT");
        getDummyList();
        helSupport_RV.setAdapter(new HelpFaqRecAdapter(context, arrayList, mListener));
        return v;
    }

    private void getDummyList() {
        arrayList = new ArrayList<>();
        arrayList.add("Take me to the app walkthrough");
        arrayList.add("I want to unsubscribe");
        arrayList.add("I didn’t reveive the refer & earn’ amount");
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

}
