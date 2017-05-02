package id.co.dsip.propampoldakalbar.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import id.co.dsip.propampoldakalbar.adapter.KomentarAdapter;
import id.co.dsip.propampoldakalbar.model.Coment;

public class ComentFragmentDialog extends DialogFragment {
    public static final String ARG_PARAM_COMENTS = "param_coments";

    private List<Coment> comments = new ArrayList<Coment>();
    private KomentarAdapter mAdapter;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.btClose) Button btClose;

    public static ComentFragmentDialog newInstance(List<Coment> comments) {
        ComentFragmentDialog fragment = new ComentFragmentDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_COMENTS, Parcels.wrap(comments));
        fragment.setArguments(args);
        return fragment;
    }

    public ComentFragmentDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.coment_fragment_dialog, container, false);

        ButterKnife.bind(this, view);

        Dialog dialog = getDialog();
        dialog.setTitle("List Komentar");
        dialog.setCancelable(false);

        if (getArguments() != null) {
            List<Coment> paramComments = Parcels.unwrap(getArguments().getParcelable(ARG_PARAM_COMENTS));
            comments.addAll(paramComments);
        }

        mAdapter = new KomentarAdapter(getActivity(), comments);

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

        return view;
    }
}
