package com.hoplay.kay.hoplay.CoresAbstract.ChatAbstracts;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoplay.kay.hoplay.Adapters.ChatAdapter;
import com.hoplay.kay.hoplay.Adapters.CommonAdapter;
import com.hoplay.kay.hoplay.Adapters.ViewHolders;
import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.GameProvider;
import com.hoplay.kay.hoplay.Models.PlayerModel;
import com.hoplay.kay.hoplay.Models.RequestModel;
import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.Models.ChatMessage;
import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.HandlerCondition;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import emojicon.EmojiconEditText;
import emojicon.EmojiconGridView;
import emojicon.EmojiconsPopup;
import emojicon.emoji.Emojicon;
import emojicon.emoji.Objects;



public abstract class Chat extends AppCompatActivity {

    protected enum TYPE {
        PUBLIC,PRIVATE
    };

    /***************************************/

    // App
     protected App app;

    // layout contents
    protected EmojiconEditText messageET;
    protected EmojiconsPopup emojiconsPopup;


    protected ImageView emojiBtn , closeChatImageView;
    protected ImageButton sendBtn;

    protected View rootView;
    protected ListView messagesContainer;


    protected CircleImageView roomPictureImageView;
    protected TextView roomNameTextView;
    protected TextView roomSubtitleTextView;

    // adapter impalement
    protected ChatAdapter adapter;
    protected ArrayList<ChatMessage> chatHistory = new ArrayList<ChatMessage>();
    protected HashMap<String,ChatMessage> chatMessages = new HashMap<String,ChatMessage>();


    protected TYPE chatType;

    protected HashMap<String,PlayerModel> playerOnChat = new HashMap<>();

    protected String chatRoomKey,roomName = null, roomPictureUrl = null, chatRoomType = null;
    protected String opponentKey;


    // Variables for add friend menu
    protected boolean isFriend = false ;
    protected boolean isDone = false;
    protected String opponentName = "";
    private boolean requestMenuAdded = false;


        // TOOLBAR : ROOM
    Toolbar toolbar ;


//    // Game providers recyclerview components
//    private RecyclerView gameProviderRecyclerView;
//    RecyclerView.LayoutManager mLayoutManager;
//    RecyclerView.Adapter mAdapter;
//    protected ArrayList<GameProvider> providersList = new ArrayList<GameProvider>();


    @Override
    protected void onStart() {
        super.onStart();
        App.chatActivityIsActive = true ;
    }

    @Override
    protected void onStop() {
        super.onStop();
        App.chatActivityIsActive = false;
    }

    /***************************************/


    // these main methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // Scren orientation :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);






        app = App.getInstance();

        //load message
        Intent i = getIntent();
        chatRoomKey = i.getStringExtra("room_key");
        chatRoomType = i.getStringExtra("room_type");
        roomName = i.getStringExtra("room_name");
        roomPictureUrl = i.getStringExtra("room_picture");
        opponentKey = i.getStringExtra("friend_key");



        chatType = (chatRoomType.equals(FirebasePaths.FIREBASE_PRIVATE_ATTR)) ? TYPE.PRIVATE:TYPE.PUBLIC;

        // Users toolbar :
        toolbar = (Toolbar) findViewById(R.id.users_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        initControls();


        // set up chat app mechanisms
        setupChat();



    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        if(chatType == TYPE.PUBLIC)
        {
            Log.i("======>","ITS PUBLIC ");
            getMenuInflater().inflate(R.menu.menu_request_chat,menu);
        }
        else {
            CallbackHandlerCondition callback = new CallbackHandlerCondition() {
                @Override
                public boolean callBack() {


                    if(isDone)
                    {

                        if (chatType == TYPE.PRIVATE )
                        {
                            Log.i("======>","ITS PRIVATE ");
                            if (!isFriend)
                                getMenuInflater().inflate(R.menu.menu_chat_with_add_friend, menu);
                            else
                                getMenuInflater().inflate(R.menu.menu_chat, menu);

                        }



                    }
                    return isDone;
                }
            };
            new HandlerCondition(callback, 0);
        }




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // view profile action
        if (id == R.id.view_profile_menu_action) {
            viewPorfileProccess();
            return true;
        }

        // view lobby action
        if (id == R.id.view_lobby_menu_action) {
            viewLobbyProccess();
            return true;
        }

        // Add friend
        if (id == R.id.add_friend_menu_action) {
            addThisUserAsFriend();
            Toast.makeText(getApplicationContext(),opponentName+" Successfully added",Toast.LENGTH_LONG).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



//    private void setUpGamesProvidersRecyclerView()
//    {
//        gameProviderRecyclerView = (RecyclerView)  findViewById(R.id.game_providers_recyclerview_chat);
//
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        gameProviderRecyclerView.setHasFixedSize(true);
//
//        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        gameProviderRecyclerView.setLayoutManager(mLayoutManager);
//
//
//        mAdapter = createAdapter();
//        gameProviderRecyclerView.setAdapter(mAdapter);
//    }
//
//
//    private CommonAdapter<GameProvider> createAdapter() {
//        return new CommonAdapter<GameProvider>(providersList, R.layout.game_provider_model) {
//
//            @Override
//            public ViewHolders OnCreateHolder(View v) {
//                return null;
//            }
//
//            @Override
//            public void OnBindHolder(ViewHolders holder, GameProvider model, int position) {
//
//            holder.setTitle(model.getGameProviderAcc());
//
//            }
//
//
//        };
//    }
//
//
//


    // this method to set up a chat layout containts
    private void initControls() {

        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        // init a layout contatins



        rootView = findViewById(R.id.chatContainer);
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EmojiconEditText) findViewById(R.id.messageEdit);
        messageET.setTypeface(playregular);

        sendBtn = (ImageButton) findViewById(R.id.chatSendButton);
        emojiBtn = (ImageView) findViewById(R.id.emojiBtn);

        adapter = new ChatAdapter(this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        roomNameTextView = (TextView)findViewById(R.id.title_users_toolbar);
        roomSubtitleTextView = (TextView)findViewById(R.id.subtitle_users_toolbar);
        roomSubtitleTextView.setText("");
        roomPictureImageView = (CircleImageView)findViewById(R.id.user_in_toolbar_imageview);


        closeChatImageView = (ImageView) findViewById(R.id.close_chat_imageview);

        // set up emojis
        setupPopupEmoji();
        // add some dummy texts for test
       // loadDummyHistory();


        // implement a send button to send text
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                sendMessageToFirebase(messageText);

            }
        });

        // implement text watcher
        messageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
               // sendBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sendiconmessagein));
                sendBtn.setImageResource(R.drawable.ic_send_focused_24dp);
            }
        });


        // close chat :
        closeChatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        // View user profile if the room is private , view lobby if the room is public
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chatType == TYPE.PRIVATE)
                {
                    viewPorfileProccess();
                }
                else {
                    viewLobbyProccess();
                }
            }
        });


        // set up game providers recyclerview
//        setUpGamesProvidersRecyclerView();


    }

    // this method for adding new message to adapter and display it
    protected void addMessage(ChatMessage message) {

        //check if this message is empty
        if (isMessageEmpty(message.getMessage()) ||  chatMessages.containsKey(message.getId())) {
            return;
        }

        // add to adapter and display it
        chatMessages.put(message.getId(),message);
        adapter.add(message);
        adapter.notifyDataSetChanged();

        // scroll the message layout container
        scroll();

    }

    // this method for send message , execute only when a user click on send button
    protected boolean sendMessage(String message) {


        sendBtn.setImageResource(R.drawable.ic_send_not_focused_24dp);

        // check if the message is empty
        if(isMessageEmpty(message))
            return false;
        messageET.setText("");

        return true;
    }

    // this method for receive message , execute only when a user receive a message
    protected void receiveMessage(Objects ... args) {

    }

    // check if message is empty
    private boolean isMessageEmpty(String message) {
        return message.equals("\\s++") ||message.equals("")||message ==null || TextUtils.isEmpty(message);
    }

    private void scroll() {

        messagesContainer.setSelection(messagesContainer.getCount());
    }

    // this method to set up a emoji
    private void setupPopupEmoji() {
        emojiconsPopup = new EmojiconsPopup(rootView, getApplicationContext());
        emojiconsPopup.setSizeForSoftKeyboard();

        //If the emoji popup is dismissed, change emojiButton to smiley icon
        emojiconsPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                emojiBtn.setImageResource(R.drawable.ic_sentiment_neutral_32dp);

            }
        });

        //If the text keyboard closes, also dismiss the emoji popup
        emojiconsPopup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {
            @Override
            public void onKeyboardOpen(int keyBoardHeight) {
            }

            @Override
            public void onKeyboardClose() {
                if (emojiconsPopup.isShowing())
                    emojiconsPopup.dismiss();
            }
        });
        //On emoji clicked, add it to edittext
        emojiconsPopup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {
            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (messageET == null || emojicon == null) {
                    return;
                }
                int start = messageET.getSelectionStart();
                int end = messageET.getSelectionEnd();
                if (start < 0) {
                    messageET.append(emojicon.getEmoji());
                } else {
                    messageET.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });
        //On backspace clicked, emulate the KEYCODE_DEL key event
        emojiconsPopup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {
            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                messageET.dispatchKeyEvent(event);
            }
        });


        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        emojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if (!emojiconsPopup.isShowing()) {
                    //If keyboard is visible, simply show the emoji popup
                    if (emojiconsPopup.isKeyBoardOpen()) {
                        emojiconsPopup.showAtBottom();
                        emojiBtn.setImageResource(R.drawable.ic_keyboard_hide_18dp);
                    }
                    //else, open the text keyboard first and immediately after that show the emoji popup
                    else {
                        messageET.setFocusableInTouchMode(true);
                        messageET.requestFocus();
                        emojiconsPopup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(messageET, InputMethodManager.SHOW_IMPLICIT);
                        emojiBtn.setImageResource(R.drawable.ic_sentiment_neutral_32dp);
                    }
                }
                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else {
                    emojiconsPopup.dismiss();
                }
            }
        });
    }


    protected void setRoomDetails(String roomName,String roomPicture) {

        roomNameTextView.setText(roomName);

        roomPictureImageView.setImageResource(R.drawable.profile_default_photo);
        app.loadingImage(roomPictureImageView,roomPicture);

    }

    public void addUserToSubtitle(String username)
    {
        String currentSub = roomSubtitleTextView.getText().toString().trim();
        if(!currentSub.isEmpty())
        currentSub+=" , "+username;
        else
            currentSub+=username;

        roomSubtitleTextView.setText(currentSub);
    }



    public void leaveMessage(String username,String message)
    {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId("");
        chatMessage.setUsername("");
        chatMessage.setMessage(message);
        chatMessage.setTimestamp(0);

        // add to adapter and display it
        chatMessages.put("",chatMessage);
        adapter.add(chatMessage);
        adapter.notifyDataSetChanged();
    }

    // this abstract method is for implements the chat mechinsim
    protected abstract void setupChat();
    protected abstract void sendMessageToFirebase(String message);
    protected abstract void viewPorfileProccess();
    protected abstract void viewLobbyProccess();
    protected abstract void checkIsFriend(String Key);
    protected abstract void addThisUserAsFriend();

}
