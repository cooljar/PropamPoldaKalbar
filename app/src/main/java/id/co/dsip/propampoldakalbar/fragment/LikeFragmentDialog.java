package id.co.dsip.propampoldakalbar.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.adapter.LikeAdapter;
import id.co.dsip.propampoldakalbar.model.Like;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikeFragmentDialog extends DialogFragment {

    public static final String ARG_PARAM_LIKES = "param_likes";

    private List<Like> likes = new ArrayList<Like>();
    private LikeAdapter mAdapter;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.btClose) Button btClose;

    public static LikeFragmentDialog newInstance(List<Like> likes) {
        LikeFragmentDialog fragment = new LikeFragmentDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_LIKES, Parcels.wrap(likes));
        fragment.setArguments(args);
        return fragment;
    }

    public LikeFragmentDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.like_fragment_dialog, container, false);

        ButterKnife.bind(this, view);

        Dialog dialog = getDialog();
        dialog.setTitle("Yang menyukai inihhi");
        dialog.setCancelable(false);

        if (getArguments() != null) {
            List<Like> paramLikes = Parcels.unwrap(getArguments().getParcelable(ARG_PARAM_LIKES));
            likes.addAll(paramLikes);

            mAdapter = new LikeAdapter(getActivity(), likes);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);

            btClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        return view;
    }

}
