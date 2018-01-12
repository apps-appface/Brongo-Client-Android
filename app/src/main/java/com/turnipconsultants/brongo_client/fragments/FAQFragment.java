package com.turnipconsultants.brongo_client.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.FAQExpandableLVAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FAQFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.title)
    TextView toolBarTitle;

    @BindView(R.id.reset)
    TextView toolBarReset;

    @BindView(R.id.faq_ELV1)
    ExpandableListView faqELV1;


    private Context context;
    private Unbinder unbinder;

    FAQExpandableLVAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader1;
    HashMap<String, List<String>> listDataChild1;


    public FAQFragment() {
        // Required empty public constructor
    }

    public static FAQFragment newInstance(String param1, String param2) {
        FAQFragment fragment = new FAQFragment();
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
        View v = inflater.inflate(R.layout.fragment_faq, container, false);
        unbinder = ButterKnife.bind(this, v);
        context = getActivity();

        toolBarReset.setVisibility(View.GONE);
        toolBarTitle.setText("FAQ");
        prepareSection1();
        faqELV1.setAdapter(new FAQExpandableLVAdapter(context, listDataHeader1, listDataChild1));
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    private void prepareSection1() {
        listDataHeader1 = new ArrayList<String>();
        listDataChild1 = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader1.add("What is Brongo?");
        listDataHeader1.add("How do I connect with a broker?");
        listDataHeader1.add("What education does brongo provide to a broker?");
        listDataHeader1.add("Benifits of using brongo?");
        listDataHeader1.add("How is Brongo different from other real estate portals?");
        listDataHeader1.add("How do I raise multiple requests?");
        listDataHeader1.add("How do I connect with 2 or more brokers at the same time?");
        listDataHeader1.add("What should I do if I am not happy with my current broker's service?");
        listDataHeader1.add("What will happer if the broker drops me during the deal?");
        listDataHeader1.add("What are the roles and responsibilities of a broker?");
        listDataHeader1.add("Why should I pay the commission?");
        listDataHeader1.add("What should I do after execution of the deal?");
        listDataHeader1.add("How and when do I pay?");

        // Adding child data
        List<String> para1 = new ArrayList<String>();
        para1.add("Brongo is a technical platform which helps people to connect with the best local and\n" +
                "curated broker to fulfil your needs in real estate.");

        List<String> para2 = new ArrayList<String>();
        para2.add("Download and register on Brongo app. Fill all your real estate requirements and click on\n" +
                "&#39;connect with the best local broker\u009D&#39;. After this process, Brongo will give you up to 3 best\n" +
                "local brokers you can choose from to fulfil your requirements. Choose your brokers based\n" +
                "on their performance track record.");

        List<String> para3 = new ArrayList<String>();
        para3.add("Real estate is a vast industry and we are specialising it based on the micromarket. On our\n" +
                "platform, broker will know the details of the projects being launched, supply, demands,\n" +
                "pricing and all other aspects of real estate market. We also provide them with soft skills\n" +
                "training.");
        List<String> para4 = new ArrayList<String>();
        para4.add("Access to curated, background verified local brokers. Real-time connection with local\n" +
                "brokers. Commission agreed before executing the deal ensuring there are no hassles during\n" +
                "the closure of the deal. User can view the feedback and ratings of a broker before choosing\n" +
                "them. Options to communicate with the Brokers through Brongo Chat helps to maintain\n" +
                "clients identity and discretion. Users have options to get a new broker if they are not\n" +
                "satisfied with their present one. Brongo support team to help and support from the start till\n" +
                "the closure of the deal.");
        List<String> para5 = new ArrayList<String>();
        para5.add("Brongo provides an organised and secure way to connect with the best curated and verified\n" +
                "brokers in real time.");

        List<String> para6 = new ArrayList<String>();
        para6.add("After submitting your 1st real estate requirement, you will be redirected to a new page with\n" +
                "all your requirement and broker details. At the bottom of this page you will find &#39;start a new\n" +
                "search&#39;\u009D, by clicking on it you can raise a new request. Follow the same process to raise\n" +
                "multiple requests.");

        List<String> para7 = new ArrayList<String>();
        para7.add("After submitting your real estate requirements, you will be redirected to a new page with all\n" +
                "your requirements and broker details. At the bottom of this page you will find &#39;start a new\n" +
                "search&#39;\u009D, clicking on it you can raise a new request. Follow the same process to connect with\n" +
                "2 or more brokers.");

        List<String> para8 = new ArrayList<String>();
        para8.add("You can drop the lead by clicking on &#39;drop out&#39;. After this you will receive a pop up\n" +
                "notification which will prompt you to state your &#39;personal reason&#39; or &#39;I dont like the broker&#39;.\n" +
                "Choosing the option &#39;I dont like the broker&#39; will put you in touch with the Brongo team who\n" +
                "will assign a new broker to fulfil your requirement.");
        List<String> para9 = new ArrayList<String>();
        para9.add("The Brongo team will get in touch with you to understand the reason of broker drop out and\n" +
                "will assign you a new broker who will have the potential to close your requirement.");
        List<String> para10 = new ArrayList<String>();
        para10.add("Below are the roles and responsibilities of a broker: Shortlisting properties based on your\n" +
                "requirement. Showing all the relevant properties with all correct information. Helps in\n" +
                "negotiation. Assists you with the legal process. Deal closure.");

        List<String> para11 = new ArrayList<String>();
        para11.add("Genuine professional help comes with a price. Our brokers understand the market like none\n" +
                "other. Brongo provides a completely organised and secure way to connect with the best\n" +
                "curated and verified brokers in real time environment and for that reason we believe it\n" +
                "should be paid.");

        List<String> para12 = new ArrayList<String>();
        para12.add("Congratulations!! if you have executed the deal. Below are the steps involved after the\n" +
                "execution: Step 1: Pay the brokerage: If you are selling the property: pay the brokerage to\n" +
                "the broker directly. For renting: commission should be routed through brongo. Step 2:\n" +
                "Please provide genuine feedback and rating to the broker, so other clients and brongo will\n" +
                "get to know more about the broker and take the right action accordingly.");

        List<String> para13 = new ArrayList<String>();
        para13.add("Once a broker updates his status stating the deal is closed, you will receive a notification on\n" +
                "the same. Once you agree on the status, an invoice will be generated and sent to you. If you\n" +
                "have done rental transaction:\n" +
                "You must pay commission to brongo. There are multiple modes of payment in the app\n" +
                "through which you can make the payment. If you have sold you property:\n" +
                "You pay commission directly to the broker.z");

        listDataChild1.put(listDataHeader1.get(0), para1); // Header, Child data
        listDataChild1.put(listDataHeader1.get(1), para2);
        listDataChild1.put(listDataHeader1.get(2), para3);
        listDataChild1.put(listDataHeader1.get(3), para4);
        listDataChild1.put(listDataHeader1.get(4), para5);

        listDataChild1.put(listDataHeader1.get(5), para6);
        listDataChild1.put(listDataHeader1.get(6), para7);
        listDataChild1.put(listDataHeader1.get(7), para8);
        listDataChild1.put(listDataHeader1.get(8), para9);
        listDataChild1.put(listDataHeader1.get(9), para10);


        listDataChild1.put(listDataHeader1.get(10), para11);
        listDataChild1.put(listDataHeader1.get(11), para12);
        listDataChild1.put(listDataHeader1.get(12), para13);
    }

}
