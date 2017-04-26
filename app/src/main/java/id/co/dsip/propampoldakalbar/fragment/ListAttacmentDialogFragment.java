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
import butterknife.OnClick;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.adapter.AttachmentItemAdapter;
import id.co.dsip.propampoldakalbar.adapter.ListAttachmentAdapter;
import id.co.dsip.propampoldakalbar.model.Attachment;
import id.co.dsip.propampoldakalbar.model.UserSession;
import id.co.dsip.propampoldakalbar.util.SimpleDividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListAttacmentDialogFragment extends DialogFragment {
    public static final String ARG_PARAM_ATTACHMENT = "param_attachment";
    public static final String ARG_PARAM_USER = "param_user";
    public static final String ARG_PARAM_LAYOUT_WIDTH = "param_layout_width";

    private UserSession user;
    private int layoutWidth;
    private List<Attachment> mAttachment = new ArrayList<Attachment>();
    private ListAttachmentAdapter attachmentItemAdapter;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.btClose) Button btClose;

    public static ListAttacmentDialogFragment newInstance(List<Attachment> att, UserSession user, int layoutWidth) {
        ListAttacmentDialogFragment fragment = new ListAttacmentDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_ATTACHMENT, Parcels.wrap(att));
        args.putParcelable(ARG_PARAM_USER, Parcels.wrap(user));
        args.putInt(ARG_PARAM_LAYOUT_WIDTH, layoutWidth);
        fragment.setArguments(args);
        return fragment;
    }

    public ListAttacmentDialogFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.btClose)
    public void closeDialog(View view) {
        dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_attacment_dialog, container, false);

        ButterKnife.bind(this, view);

        Dialog dialog = getDialog();
        dialog.setTitle("List Lampiran");
        dialog.setCancelable(false);

        if (getArguments() != null) {
            List<Attachment> att = (List<Attachment>) Parcels.unwrap(getArguments().getParcelable(ARG_PARAM_ATTACHMENT));
            mAttachment.addAll(att);

            user = Parcels.unwrap(getArguments().getParcelable(ARG_PARAM_USER));
            layoutWidth = getArguments().getInt(ARG_PARAM_LAYOUT_WIDTH, 0);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        attachmentItemAdapter = new ListAttachmentAdapter(getActivity(), mAttachment, getActivity(), layoutWidth);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(attachmentItemAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                    attachmentItemAdapter.onScrolled(recyclerView);
                }
            }

        });

        return view;
    }

}
