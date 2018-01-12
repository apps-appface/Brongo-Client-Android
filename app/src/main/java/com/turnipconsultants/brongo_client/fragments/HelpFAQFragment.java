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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HelpFAQFragmentItemClickListener} interface
 * to handle interaction events.
 * Use the {@link HelpFAQFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpFAQFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.title)
    TextView toolBarTitle;

    @BindView(R.id.reset)
    TextView toolBarReset;

    @BindView(R.id.helpfaq_recycler_view)
    RecyclerView helpFAQ_RV;

    private ArrayList<String> arrayList;
    private Context context;
    private Unbinder unbinder;

    private HelpFAQFragmentItemClickListener mListener;

    public HelpFAQFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpFAQFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpFAQFragment newInstance(String param1, String param2) {
        HelpFAQFragment fragment = new HelpFAQFragment();
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
        View v = inflater.inflate(R.layout.fragment_help_faq, container, false);
        unbinder = ButterKnife.bind(this, v);
        context = getActivity();

        toolBarReset.setVisibility(View.GONE);
        toolBarTitle.setText("HELP & FAQ");
        getDummyList();
        helpFAQ_RV.setAdapter(new HelpFaqRecAdapter(context, arrayList, mListener));
        return v;
    }

    private void getDummyList() {
        arrayList = new ArrayList<>();
        arrayList.add("Help & Support");
        arrayList.add("FAQ");
        arrayList.add("Your Rating");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HelpFAQFragmentItemClickListener) {
            mListener = (HelpFAQFragmentItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HelpFAQFragmentItemClickListener");
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
