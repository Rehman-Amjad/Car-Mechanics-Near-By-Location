package com.technogenis.carmechanics.UserPanal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technogenis.carmechanics.Model.BookModel;
import com.technogenis.carmechanics.R;
import com.technogenis.carmechanics.UserAdapter.PendingBookingAdapter;

import java.util.ArrayList;
import java.util.List;


public class PendingFragment extends Fragment {

    RecyclerView recycleView;
    PendingBookingAdapter adapter;
    private List<BookModel> mDataList;
    FirebaseAuth mAuth;
    String userUID;
    LinearLayout no_layout;

    SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pending, container, false);

        initFieldView(view);
        mAuth = FirebaseAuth.getInstance();
        userUID = mAuth.getCurrentUser().getUid();
        no_layout.setVisibility(View.VISIBLE);

        showDataInRecycleView();



        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                showDataInRecycleView();

                swipeLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void showDataInRecycleView()
    {

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Orders")
                .child(userUID);

        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDataList=new ArrayList<>();
        adapter = new PendingBookingAdapter(getActivity(),mDataList);
        recycleView.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists())
                {

                    if (snapshot.child("bookRequest").getValue().equals("pending"))
                    {
                        no_layout.setVisibility(View.GONE);
                        BookModel model = snapshot.getValue(BookModel.class);
                        mDataList.add(model);
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        no_layout.setVisibility(View.VISIBLE);
                    }

                }
                else
                {
                    no_layout.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists())
                {
                    if (snapshot.child("bookRequest").getValue().equals("pending"))
                    {
                        no_layout.setVisibility(View.GONE);
                        mDataList.clear();
                        recycleView.refreshDrawableState();
                        BookModel model = snapshot.getValue(BookModel.class);
                        mDataList.add(model);
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        no_layout.setVisibility(View.VISIBLE);
//                        progressDialog.dismiss();
                    }
                }
                else
                {
                    no_layout.setVisibility(View.VISIBLE);
//                    progressDialog.dismiss();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        if (progressDialog!=null)
//        {
//            progressDialog.dismiss();
//        }
    }

    private void initFieldView(View view)
    {
        recycleView = view.findViewById(R.id.pending_booking_recycleView);
        no_layout = view.findViewById(R.id.no_layout);
        swipeLayout = view.findViewById(R.id.swipeLayout);
    }
}