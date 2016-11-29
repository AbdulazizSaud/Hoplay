package com.example.kay.hoplay.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.kay.hoplay.Adapters.ChatAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.ChatMessage;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import emojicon.EmojiconEditText;
import emojicon.EmojiconGridView;
import emojicon.EmojiconsPopup;
import emojicon.emoji.Emojicon;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Kay on 10/30/2016.
 */

public class ChatActivity extends ActionBarActivity {


    private EmojiconEditText messageET;
    private EmojiconsPopup emojiconsPopup;
    private ImageView emojiBtn;

    private View  rootView;

    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private Socket socket;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

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


//       ActionBar actionBar = getSupportActionBar();
//         actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//        // TODO: Remove the redundant calls to getSupportActionBar()
//        //       and use variable actionBar instead
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);


        initControls();
         socket = App.getInstance().getSocket();
         socket.on(App.MESSAGE_EVENT, onMessage);

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


    void doPopupEmoji(){
        emojiconsPopup = new EmojiconsPopup(rootView,getApplicationContext());
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


    }

    private void initControls() {

        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EmojiconEditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);


        rootView = findViewById(R.id.chatContainer);
        emojiBtn = (ImageView) findViewById(R.id.emojiBtn);

        doPopupEmoji();

        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        emojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if(!emojiconsPopup.isShowing()){
                    //If keyboard is visible, simply show the emoji popup
                    if(emojiconsPopup.isKeyBoardOpen()){
                        emojiconsPopup.showAtBottom();
                        emojiBtn.setImageResource(R.drawable.ic_keyboard);
                    }
                    //else, open the text keyboard first and immediately after that show the emoji popup
                    else{
                        messageET.setFocusableInTouchMode(true);
                        messageET.requestFocus();
                        emojiconsPopup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(messageET, InputMethodManager.SHOW_IMPLICIT);
                        emojiBtn.setImageResource(R.drawable.emojichatbar);
                    }
                }
                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else{
                    emojiconsPopup.dismiss();
                }
            }
        });




        loadDummyHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageText = messageET.getText().toString();


                addMessage(messageText, true);

                messageET.setText("");
                sendBtn.setBackground( ContextCompat.getDrawable(getApplicationContext(),R.drawable.sendiconnomessage));

                socket.emit(App.MESSAGE_EVENT, messageText);

            }
        });

        messageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }



            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                sendBtn.setBackground( ContextCompat.getDrawable(getApplicationContext(),R.drawable.sendiconmessagein));

            }
        });


    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {

        messagesContainer.setSelection(messagesContainer.getCount());
    }

    private void loadDummyHistory() {

        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("Hi");
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("How r u doing???");
        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg1);

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for (int i = 0; i < chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }

    }

    public Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = String.valueOf(args[0]);
                    addMessage(message, false);
                }
            });
        }
    };


    public void addMessage(String message, boolean me) {

        if (TextUtils.isEmpty(message)) {
            return;
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(122);//dummy
        chatMessage.setMessage(message);
        chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatMessage.setMe(me);

        displayMessage(chatMessage);

    }

    public void toCommunityFragment(View view)
    {
        Intent i = new Intent(this,MainAppMenu.class);
        startActivity(i);
    }

}
