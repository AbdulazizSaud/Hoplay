package com.example.kay.hoplay.CoresAbstract;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.Cores.ChatCore.FindUserCore;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Models.CommunityChatModel;
import com.example.kay.hoplay.Services.CallbackHandlerCondition;
import com.example.kay.hoplay.Services.HandlerCondition;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class Community extends Fragment {

//    ListView communityListview ;


    private RecyclerView mRecyclerView;
    private ImageView bgChatImageView;

    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton newPrivateChatFloatingActionButton;

    protected HashMap<String, CommunityChatModel> communityChatModelHashMap = new HashMap<>();

    protected ArrayList<CommunityChatModel> communityUserLists = new ArrayList<CommunityChatModel>();
    protected RecyclerView.Adapter mAdapter;

    protected App app;

    public Community() {
        // Required empty public constructor
        app = App.getInstance();
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);
//        communityListview = (ListView)  view.findViewById(R.id.community_listview);

        setupRecyclerView(view);



        //  declaration BG
        bgChatImageView = (ImageView) view.findViewById(R.id.splash);

        // Community BG declaration imageview
        if (mAdapter.getItemCount() < 1)
            bgChatImageView.setVisibility(View.VISIBLE);
        else
            bgChatImageView.setVisibility(View.INVISIBLE);


        // Go to Friends List to start new private chat
        newPrivateChatFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.new_private_chat_floatingactionbutton);
        newPrivateChatFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), FindUserCore.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_up_layouts, R.anim.slide_out_up_layouts);


            }
        });


        OnStartActivity();


        return view;
    }


    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);




        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);




        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);



    }


    public void addToList(CommunityChatModel communityUserList) {

        if (communityChatModelHashMap.containsKey(communityUserList.getChatKey()))
            return;



        communityUserLists.add(communityUserList);
        communityChatModelHashMap.put(communityUserList.getChatKey(), communityUserList);
        mAdapter.notifyDataSetChanged();


        // Community BG declaration imageview
        if (mAdapter.getItemCount() < 1)
            bgChatImageView.setVisibility(View.VISIBLE);
        else
            bgChatImageView.setVisibility(View.INVISIBLE);





    }


    public void removeFromList(String key) {

        for (CommunityChatModel communityChatModel : communityUserLists) {
            if (communityChatModel.getChatKey().equals(key)) {
                communityUserLists.remove(communityChatModel);
                communityChatModelHashMap.remove(communityChatModel.getChatKey());
                mAdapter.notifyDataSetChanged();
                break;
            }
        }

        // Community BG declaration imageview
        if (mAdapter.getItemCount() < 1)
            bgChatImageView.setVisibility(View.VISIBLE);
        else
            bgChatImageView.setVisibility(View.INVISIBLE);
    }


    private CommonAdapter<CommunityChatModel> createAdapter() {
        return new CommonAdapter<CommunityChatModel>(communityUserLists, R.layout.new_user_chat_instance) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.CommunityHolder(v);
            }

            @Override
            public void OnBindHolder(final ViewHolders holder, final CommunityChatModel model, int position) {
                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                ViewHolders.CommunityHolder communityHolder = (ViewHolders.CommunityHolder) holder;



                //  Set the bottom bar chat counter for unsean messages
                MainAppMenu.setChatCounterOnBottombar(app.getChatsCounter());





                // Community BG declaration imageview
                if (mAdapter.getItemCount() < 1)
                    bgChatImageView.setVisibility(View.VISIBLE);
                else
                    bgChatImageView.setVisibility(View.INVISIBLE);


                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model, v);
                    }
                });

                holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showOnLongClickDialog(model);
                        return false;
                    }
                });


                holder.getPicture().setBorderWidth(6);
                holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.app_color));


                // Capitalize Title letters
                String chatTitle = model.getChatName();

                if (chatTitle == null)
                    return;

                String capitlizedChatTitle = chatTitle.substring(0, 1).toUpperCase() + chatTitle.substring(1);

                if (chatTitle.contains(" ")) {
                    // Capitalize game title letters
                    String cpWord = "";
                    for (int i = 0; i < capitlizedChatTitle.length(); i++) {
                        if (capitlizedChatTitle.charAt(i) == 32 && capitlizedChatTitle.charAt(i + 1) != 32) {
                            cpWord = capitlizedChatTitle.substring(i + 1, i + 2).toUpperCase() + capitlizedChatTitle.substring(i + 2);
                            capitlizedChatTitle = capitlizedChatTitle.replace(capitlizedChatTitle.charAt(i + 1), cpWord.charAt(0));
                        }
                    }
                    holder.setTitle(capitlizedChatTitle);
                } else {
                    holder.setTitle(capitlizedChatTitle);
                }


                communityHolder.setCommunitySubtitle(model.getLastMsg());
                app.loadingImage(getContext(), holder, model.getUserPictureURL());


                // Hide the counter if unseen messages less than 1 message
                if (model.getChatCounter() < 1) {
                    communityHolder.getChatCounterView().setVisibility(View.INVISIBLE);
                } else {
                    Log.i("===>","DONE");
                    communityHolder.getChatCounterView().setVisibility(View.VISIBLE);
                    communityHolder.setCounter(String.valueOf(model.getChatCounter()));
                }

                communityHolder.setCounter(String.valueOf(model.getChatCounter()));

                holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));

                new HandlerCondition(new CallbackHandlerCondition() {
                    @Override
                    public boolean callBack() {

                        holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));
                        return false;
                    }
                }, 10000);
//                Log.i("->",""+getAllUnseenMessages());






            }




        };
    }


    protected abstract void removeChatFromlist(CommunityChatModel model);

    protected abstract void OnClickHolders(CommunityChatModel model, View v);

    protected abstract void OnStartActivity();

    //    public long getAllUnseenMessages()
//    {
//        for (int i = 0  ; i < communityUserLists.size() ; i++)
//        {
//            numberOfUnseenMessages  += communityUserLists.get(i).getChatCounter();
//        }
//
//        Log.i("->" , ""+numberOfUnseenMessages);
//        return numberOfUnseenMessages;
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("Destroy", "BYEBYE ");
    }


    protected void showOnLongClickDialog(final CommunityChatModel communityChatModel) {

        final Dialog onLongClickGameDialog;
        onLongClickGameDialog = new Dialog(getContext());
        onLongClickGameDialog.setContentView(R.layout.chat_long_click_pop_up);
        onLongClickGameDialog.show();


        onLongClickGameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView verificationDeleteText;
        Button deleteYesButton, deleteNoButton;

        deleteYesButton = (Button) onLongClickGameDialog.findViewById(R.id.delete_chat_yes_button);
        deleteNoButton = (Button) onLongClickGameDialog.findViewById(R.id.delete_chat_no_button);

        Typeface sansation = Typeface.createFromAsset(getResources().getAssets(), "sansationbold.ttf");
        deleteYesButton.setTypeface(sansation);
        deleteNoButton.setTypeface(sansation);


        // Delete chat
        deleteYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeChatFromlist(communityChatModel);
                onLongClickGameDialog.dismiss();
            }
        });


        // Remove Dialog
        deleteNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLongClickGameDialog.dismiss();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = onLongClickGameDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }


}

