package com.example.kay.hoplay.Fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.example.kay.hoplay.Activities.ChatActivity;
import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Common.ViewHolders;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.CommunityUserList;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends Fragment {

//    ListView communityListview ;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<CommunityUserList> communityUserLists=new ArrayList<CommunityUserList>();

    private String myAccountUser="Test";

    public CommunityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false) ;
//        communityListview = (ListView)  view.findViewById(R.id.community_listview);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager


        // test
        String picUrl = "https://s13.postimg.org/puvr2r9tz/test_user_copy.jpg";
        String username = "Bakatsuki";
        String lastMessage = "Bakatsuki has joined your request click to replay ^^";
        App.getInstance().insertIntoCASQL(username,lastMessage,picUrl);
        //


        getAdapterData();

        ///
        mAdapter = createAdapter();

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        return view;
    }


    private CommonAdapter<CommunityUserList> createAdapter(){
        return new CommonAdapter<CommunityUserList>(communityUserLists,R.layout.new_user_message) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.CommunityHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, final CommunityUserList model) {
                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                ImageLoader loader = App.getInstance().getImageLoader();
                ViewHolders.CommunityHolder communityHolder = (ViewHolders.CommunityHolder)holder;


                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = model.getReceiverID();
                        Intent i = new Intent(v.getContext(),ChatActivity.class);
                        i.putExtra("receiverUsername",id);
                        i.putExtra("myUsername",myAccountUser);

                        v.getContext().startActivity(i);
                    }
                });

                if(model.getUserPictureURL().length() > 0){
                    loader.get(model.getUserPictureURL(),
                            ImageLoader.getImageListener(
                                    holder.getPicture()
                                    ,R.drawable.profile_default_photo
                                    ,R.drawable.profile_default_photo));

                } else {
                    CircularImageView picture = holder.getPicture();
                    picture.setImageResource(R.drawable.profile_default_photo);
                    holder.setPicture(picture);
                }
                holder.setTitle(model.getFullName());
                communityHolder.setCommunitySubtitle(model.getLastMsg());
                holder.setTime(model.getLastMsgDate());
            }
        };
    }

    private void getAdapterData(){
        try {
            Cursor cursor = App.getInstance().getCASQL();
            int recIDIndex = cursor.getColumnIndex("receiver_ID");
            int picURLIndex = cursor.getColumnIndex("receiver_Picture");
            int lastMessageIndex = cursor.getColumnIndex("last_message");


            cursor.moveToFirst();
            while (cursor != null) {

                String receiverID = cursor.getString(recIDIndex);
                String picUrl = cursor.getString(picURLIndex);
                String lastMessage = cursor.getString(lastMessageIndex);

                CommunityUserList CUL = new CommunityUserList(receiverID, receiverID, picUrl);
                CUL.setLastMsg(lastMessage);

                communityUserLists.add(CUL);

                cursor.moveToNext();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
