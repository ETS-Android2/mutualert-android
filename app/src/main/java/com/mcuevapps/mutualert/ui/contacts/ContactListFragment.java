package com.mcuevapps.mutualert.ui.contacts;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.data.ContactViewModel;
import com.mcuevapps.mutualert.retrofit.response.AlertContact;

import java.util.List;

public class ContactListFragment extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ContactRecyclerViewAdapter adapter;
    List<AlertContact> contactList;
    ContactViewModel contactViewModel;

    public ContactListFragment() {
    }

    public static ContactListFragment newInstance() {
        ContactListFragment fragment = new ContactListFragment();
        /*
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        */
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactViewModel = new ViewModelProvider(getActivity()).get(ContactViewModel.class);

        if (getArguments() != null) {
            //mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        // Set the adapter
        //if (view instanceof RecyclerView) {
            Context context = view.getContext();

            swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(true);
                    loadNewData();
                }
            });

            recyclerView = view.findViewById(R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new ContactRecyclerViewAdapter(
                    getActivity(),
                    contactList
            );
            recyclerView.setAdapter(adapter);
            loadContactData();
        //}
        return view;
    }

    private void loadContactData() {
        contactViewModel.getContacts().observe(getActivity(), new Observer<List<AlertContact>>() {
            @Override
            public void onChanged(@Nullable List<AlertContact> contacts) {
                contactList = contacts;
                adapter.setData(contactList);
            }
        });
    }

    private void loadNewData(){
        contactViewModel.getNewContacts().observe(getActivity(), new Observer<List<AlertContact>>() {
            @Override
            public void onChanged(List<AlertContact> contacts) {
                contactList = contacts;
                swipeRefreshLayout.setRefreshing(false);
                adapter.setData(contactList);
                contactViewModel.getNewContacts().removeObserver(this);
            }
        });
    }
}