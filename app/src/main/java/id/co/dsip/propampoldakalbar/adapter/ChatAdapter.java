package id.co.dsip.propampoldakalbar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.model.Chat;

/**
 * Created by japra_awok on 22/04/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    private Context context;
    private List<Chat> mChat;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.mChat = chatList;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatAdapter.ViewHolder holder, int position) {
        final Chat chat = mChat.get(position);
        holder.tvMessage.setText(chat.message);
        holder.tvCreated.setText(chat.created_at);

        // if message is mine then align to right
        if (chat.isMine) {
            holder.bubble_layout.setBackgroundResource(R.drawable.bubble2);
            holder.bubble_layout_parent.setGravity(Gravity.RIGHT);
        }else{
            holder.bubble_layout.setBackgroundResource(R.drawable.bubble1);
            holder.bubble_layout_parent.setGravity(Gravity.LEFT);
        }
    }

    @Override
    public int getItemCount() {
        return mChat == null ? 0 : mChat.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mChat.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Chat> list) {
        mChat.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.bubble_layout_parent) LinearLayout bubble_layout_parent;
        @BindView(R.id.bubble_layout) LinearLayout bubble_layout;
        @BindView(R.id.tvMessage) TextView tvMessage;
        @BindView(R.id.tvCreated) TextView tvCreated;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
