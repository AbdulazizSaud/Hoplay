package com.example.kay.hoplay.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kay.hoplay.R;
import de.hdodenhof.circleimageview.CircleImageView;

import emojicon.EmojiconTextView;




public abstract class ViewHolders extends RecyclerView.ViewHolder {



    protected CircleImageView picture;
    protected TextView title,subtitle,time , pc , ps , xbox;
    protected View view;


    TextView numberOfPlayers;

    public ViewHolders(View v) {
        super(v);
        this.view = v;
    }

    public View getView(){

    return view;

    }

    public static class CommunityHolder extends ViewHolders {
        EmojiconTextView chatLastMessage;
        TextView chatNewMessagesCount;
        public CommunityHolder(View v) {
            super(v);

            picture = (CircleImageView) v.findViewById(R.id.chatOpponent);


            title =  (TextView) v.findViewById(R.id.chatOpponentFullname);
            Typeface sansation = Typeface.createFromAsset(title.getContext().getAssets() ,"sansationbold.ttf");
            Typeface opensans = Typeface.createFromAsset(title.getContext().getAssets() ,"opensans.ttf");
            title.setTypeface(opensans);


            time =  (TextView) v.findViewById(R.id.chatLastMessageAgo);
            time.setTypeface(sansation);

            chatNewMessagesCount =  (TextView) v.findViewById(R.id.chatNewMessagesCount);
            chatNewMessagesCount.setTypeface(sansation);

            chatLastMessage = (EmojiconTextView) v.findViewById(R.id.chatLastMessage);
            chatLastMessage.setTypeface(sansation);

        }
        public void setCommunitySubtitle(String subtitle){
            this.chatLastMessage.setText(subtitle);
        }

        public void setCounter(String counter){
            this.chatNewMessagesCount.setText(counter);
        }

    }

    public static class SavedRequestHolder extends ViewHolders {
        public SavedRequestHolder(View v) {
            super(v);

            title =  (TextView) v.findViewById(R.id.game_name_saved_request_textview);

            // In case you want to play with the fonts and fonts color :
            Typeface sansation = Typeface.createFromAsset(title.getContext().getAssets() ,"sansationbold.ttf");
            title.setTypeface(sansation);
            title.setTextColor(title.getResources().getColor(R.color.pc_color));


            subtitle =  (TextView) v.findViewById(R.id.request_description_saved_request_textview);
            subtitle.setTypeface(sansation);

            picture = (CircleImageView) v.findViewById(R.id.game_photo_saved_request_circularimageview);

            numberOfPlayers = (TextView) v.findViewById(R.id.number_of_players_saved_requests_textview);
            numberOfPlayers.setTypeface(sansation);
        }
    }
    public static class RecentGameHolder extends ViewHolders {

        public RecentGameHolder(View v) {
            super(v);

            title =  (TextView) v.findViewById(R.id.game_name_recent_activity_textview);
            Typeface sansation = Typeface.createFromAsset(title.getContext().getAssets() ,"sansationbold.ttf");
            title.setTypeface(sansation);

            subtitle =  (TextView) v.findViewById(R.id.activity_description_textview);
            subtitle.setTypeface(sansation);
            picture = (CircleImageView) v.findViewById(R.id.game_photo_recent_activity_circularimageview);
            time = (TextView) v.findViewById(R.id.activity_date_textview);
            time.setTypeface(sansation);
        }
    }
    public static class FriendListHolder extends ViewHolders {

        public FriendListHolder(View v) {
            super(v);

            picture = (CircleImageView)v.findViewById(R.id.friend_profile_photo_circleimageview);
            title  = (TextView)v.findViewById(R.id.friend_nickname_friends_list_textview);
        }
    }
    public static class UserGameHolder extends ViewHolders {

        public UserGameHolder(View v) {
            super(v);

            title =  (TextView) v.findViewById(R.id.game_name_add_game_textview);
            picture = (CircleImageView) v.findViewById(R.id.game_photo_add_game_circleimageview);
            subtitle = (TextView) v.findViewById(R.id.game_available_platforms_textview);
            pc = (TextView) v.findViewById(R.id.pc_available_game_instance_textview);
            ps = (TextView) v.findViewById(R.id.ps_available_game_instance_textview);
            xbox = (TextView) v.findViewById(R.id.xbox_available_game_instance_textview);




        }
    }

    public void setPicture(CircleImageView picture) {
        this.picture = picture;
    }
    public CircleImageView getPicture() {
        return picture;
    }
    public TextView getTitleView() {
        return title;
    }
    public void setTitleView(TextView title) {
        this.title = title;
    }
    public TextView getSubtitleView() {
        return subtitle;
    }
    public void setSubtitleView(TextView subtitle) {
        this.subtitle = subtitle;
    }
    public TextView getTimeView() {
        return time;
    }
    public void setTimeView(TextView time) {
        this.time = time;
    }
    public String getNumberOfPlayers() {
        return numberOfPlayers.getText().toString().trim();
    }
    public void setNumberOfPlayers(String numberOfPlayers) {
        this.numberOfPlayers.setText(numberOfPlayers);
    }
    public TextView getPcTextView()
    {return pc;}

    public TextView getPsTextView()
    {return ps;}
    public TextView getXboxTextView()
    {return xbox;}
    public void setTitle(String title){
        this.title.setText(title);
    }
    public void setSubtitle(String subtitle){
        this.subtitle.setText(subtitle);
    }
    public void setTime(String time){
        this.time.setText(time);
    }

}
