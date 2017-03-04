package com.example.kay.hoplay.Chat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.kay.hoplay.Activities.MainMenu.MainAppMenuActivity;
import com.example.kay.hoplay.Adapters.ChatAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.ChatMessage;
import com.pkmmte.view.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import emojicon.EmojiconEditText;
import emojicon.EmojiconGridView;
import emojicon.EmojiconsPopup;
import emojicon.emoji.Emojicon;
import emojicon.emoji.Objects;

/**
 * Created by azoz-pc on 2/4/2017.
 */

public abstract class Chat extends AppCompatActivity implements ChatInterface {

    /***************************************/

    // App
     protected App app;

    // layout contents
    protected EmojiconEditText messageET;
    protected EmojiconsPopup emojiconsPopup;


    protected ImageView emojiBtn;
    protected Button sendBtn;

    protected View rootView;
    protected ListView messagesContainer;


    protected CircleImageView roomPicture;
    protected TextView roomName;

    // adapter impalement
    protected ChatAdapter adapter;
    protected ArrayList<ChatMessage> chatHistory = new ArrayList<ChatMessage>();

    // .....
    protected String myUsername = null;
    protected String receiverUsername = null;


    /***************************************/


    // these main methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // Scren orientation :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // Users toolbar :
        Toolbar toolbar = (Toolbar) findViewById(R.id.users_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        app = App.getInstance();

        initControls();

        // set up chat app mechanisms
        setupChat();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // this method to set up a chat layout containts
    private void initControls() {

        // init a layout contatins
        rootView = findViewById(R.id.chatContainer);
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EmojiconEditText) findViewById(R.id.messageEdit);


        sendBtn = (Button) findViewById(R.id.chatSendButton);
        emojiBtn = (ImageView) findViewById(R.id.emojiBtn);

        adapter = new ChatAdapter(this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        roomName = (TextView)findViewById(R.id.user_name_in_toolbar_textview);
        roomPicture = (CircleImageView)findViewById(R.id.user_in_toolbar_imageview);

        // set up emojis
        setupPopupEmoji();
        // add some dummy texts for test
       // loadDummyHistory();


        // implement a send button to send text
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                sendMessage(messageText);

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
                sendBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sendiconmessagein));

            }
        });

    }

    // this method for adding new message to adapter and display it
    protected void addMessage(String id,String message, boolean me) {

        //check if this message is empty
        if (isMessageEmpty(message)) {
            return;
        }

        // set a message
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(id);
        chatMessage.setMessage(message);
        chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatMessage.setMe(me);

        // add to adapter and display it
        adapter.add(chatMessage);
        adapter.notifyDataSetChanged();

        // scroll the message layout container
        scroll();

    }

    // this method for send message , execute only when a user click on send button
    protected void sendMessage(String message) {

        // check if the message is empty
        if(isMessageEmpty(message))
            return;

        // add message to adapter
        //addMessage(myUsername,message, true);

        messageET.setText("");
        sendBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sendiconnomessage));

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

                emojiBtn.setImageResource(R.drawable.emojichatbar);

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
                        emojiBtn.setImageResource(R.drawable.ic_keyboard);
                    }
                    //else, open the text keyboard first and immediately after that show the emoji popup
                    else {
                        messageET.setFocusableInTouchMode(true);
                        messageET.requestFocus();
                        emojiconsPopup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(messageET, InputMethodManager.SHOW_IMPLICIT);
                        emojiBtn.setImageResource(R.drawable.emojichatbar);
                    }
                }
                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else {
                    emojiconsPopup.dismiss();
                }
            }
        });
    }

    // this method add some testing stuffs
    private void loadDummyHistory() {

        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId("dummyID");
        msg.setMe(false);
        msg.setMessage("Hi");
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId("dummyID");
        msg1.setMe(false);
        msg1.setMessage("How r u doing???");
        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg1);


        for (int i = 0; i < chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            // add to adapter and display it
            adapter.add(msg1);
            adapter.notifyDataSetChanged();

            // scroll the message layout container
            scroll();
        }

    }


    // this method to switch the current to community frag
    public void toCommunityFragment(View view) {
//        Intent i = new Intent(this, MainAppMenuActivity.class);
//        startActivity(i);
       finish();
    }

    // this abstract method is for implements the chat mechinsim
    public abstract void setupChat();

}
