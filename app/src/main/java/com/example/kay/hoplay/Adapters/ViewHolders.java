package com.example.kay.hoplay.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kay.hoplay.R;
import de.hdodenhof.circleimageview.CircleImageView;

import emojicon.EmojiconTextView;




public abstract class ViewHolders extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener {



    protected CircleImageView picture;
    protected TextView title,subtitle,time , subtitle2 ;
    protected View view;
    protected ImageView subtitleImageview;






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

            Typeface playbold = Typeface.createFromAsset(title.getContext().getAssets(), "playbold.ttf");
            Typeface playregular = Typeface.createFromAsset(title.getContext().getAssets(), "playregular.ttf");


            title.setTypeface(playbold);


            time =  (TextView) v.findViewById(R.id.chatLastMessageAgo);
            time.setTypeface(playregular);

            chatNewMessagesCount =  (TextView) v.findViewById(R.id.chatNewMessagesCount);
            chatNewMessagesCount.setTypeface(playbold);

            chatLastMessage = (EmojiconTextView) v.findViewById(R.id.chatLastMessage);
            chatLastMessage.setTypeface(playregular);

        }
        public void setCommunitySubtitle(String subtitle){
            this.chatLastMessage.setText(subtitle);
        }

        public void setCounter(String counter){
            this.chatNewMessagesCount.setText(counter);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }


    public static class GameProviderHolder extends  ViewHolders{
        public GameProviderHolder(View v){
            super(v);
            title = (TextView) v.findViewById(R.id.game_provider_holder_text);
            Typeface playregular = Typeface.createFromAsset(title.getContext().getAssets() ,"playregular.ttf");
            title.setTypeface(playregular);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
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

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
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

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public static class FriendListHolder extends ViewHolders {

        public FriendListHolder(View v) {
            super(v);

            picture = (CircleImageView)v.findViewById(R.id.friend_profile_photo_circleimageview);
            title  = (TextView)v.findViewById(R.id.friend_nickname_friends_list_textview);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public static class PlayerHolder extends ViewHolders {

        public PlayerHolder(View v) {
            super(v);

            picture = (CircleImageView)v.findViewById(R.id.player_instance_circleimageview);
            title  = (TextView)v.findViewById(R.id.player_instance_username_textview);
            subtitle = (TextView) v.findViewById(R.id.player_game_provider_textview);
            subtitle2 = (TextView) v.findViewById(R.id.player_game_provider_account_textview);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }



    public static class UserGameHolder extends ViewHolders {

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        public enum Platform {
            XBOX,PC,PS
        }

        TextView  pc , ps , xbox;

        public UserGameHolder(View v) {
            super(v);

            title =  (TextView) v.findViewById(R.id.game_name_add_game_textview);
            picture = (CircleImageView) v.findViewById(R.id.game_photo_add_game_circleimageview);
            subtitle = (TextView) v.findViewById(R.id.game_available_platforms_textview);
            pc = (TextView) v.findViewById(R.id.pc_available_game_instance_textview);
            ps = (TextView) v.findViewById(R.id.ps_available_game_instance_textview);
            xbox = (TextView) v.findViewById(R.id.xbox_available_game_instance_textview);


        }



        public TextView getPlatformTextView(Platform platform){
            TextView view = pc;
            switch (platform)
            {
                case PC:view =pc;break;
                case XBOX:view =xbox;break;
                case PS:view =ps;break;

            }
            return view;
        }

        public TextView getPcTextView()
        {return pc;}

        public TextView getPsTextView()
        {return ps;}
        public TextView getXboxTextView()
        {return xbox;}


    }

    public static class SearchResultsHolder extends  ViewHolders {

        public SearchResultsHolder(View v) {
            super(v);

            title =  (TextView) v.findViewById(R.id.game_name_request_model);
            picture = (CircleImageView) v.findViewById(R.id.game_photo_request_model);
            subtitle = (TextView) v.findViewById(R.id.description_request_model);
            numberOfPlayers = (TextView) v.findViewById(R.id.number_of_players_request_model);
            subtitleImageview = (ImageView) v.findViewById(R.id.match_type_request_model);
            subtitle2 = (TextView) v.findViewById(R.id.requester_request_model);
            time = (TextView) v.findViewById(R.id.time_stamp_request_model);

        }


        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }


    public TextView getTitle() {
        return title;
    }

    public TextView getSubtitle() {
        return subtitle;
    }

    public TextView getTime() {
        return time;
    }

    public TextView getSubtitle2() {
        return subtitle2;
    }

    public ImageView getSubtitleImageview() {
        return subtitleImageview;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public void setSubtitle(TextView subtitle) {
        this.subtitle = subtitle;
    }



    public void setSubtitle2(String subtitle2) {
        this.subtitle2.setText(subtitle2);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setSubtitleImageview(ImageView subtitleImageview) {
        this.subtitleImageview = subtitleImageview;
    }

    public void setNumberOfPlayers(TextView numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
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
