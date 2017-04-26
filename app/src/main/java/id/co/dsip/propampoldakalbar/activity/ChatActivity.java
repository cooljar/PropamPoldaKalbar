package id.co.dsip.propampoldakalbar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.adapter.ChatAdapter;
import id.co.dsip.propampoldakalbar.model.Chat;
import id.co.dsip.propampoldakalbar.model.Polisi;
import id.co.dsip.propampoldakalbar.model.UserSession;

public class ChatActivity extends AppCompatActivity {

    public static final String REKAN_PARCEL = "rekan_parcel";
    public static final String USER_SESSION_PARCEL = "user_session_parcel";

    private Polisi saya;
    private Polisi mRekan;
    private UserSession user;
    private List<Chat> mChat = new ArrayList<>();
    private ChatAdapter mAdapter;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.btSendMessage) ImageButton btSendMessage;
    @BindView(R.id.etMessage) EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mRekan = Parcels.unwrap(intent.getParcelableExtra(REKAN_PARCEL));
        user = Parcels.unwrap(intent.getParcelableExtra(USER_SESSION_PARCEL));

        saya = new Polisi(
                null, user.nama, user.nrp, user.hp, "", "", "", "fcm_key", user.pangkat,
                user.jabatan, user.penempatan, null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ChatAdapter(this, mChat);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.btSendMessage)
    public void sendChat(View view){
        String message = etMessage.getText().toString();

        if(!message.equalsIgnoreCase("")){
            String created_at = "2017-04-22 13:00:00";
            Chat newChat = new Chat(saya, mRekan, message, created_at, true);

            mChat.add(newChat);
            mAdapter.notifyDataSetChanged();
            etMessage.setText("");
        }
    }
}
