package id.co.dsip.propampoldakalbar.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.parceler.Parcels;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.adapter.KomentarAdapter;
import id.co.dsip.propampoldakalbar.model.Coment;
import id.co.dsip.propampoldakalbar.model.LapGiat;
import id.co.dsip.propampoldakalbar.model.UserSession;
import id.co.dsip.propampoldakalbar.rest_api.ErrorMessage;
import id.co.dsip.propampoldakalbar.rest_api.MyOkHttpInterceptor;
import id.co.dsip.propampoldakalbar.rest_api.ServiceGenerator;
import id.co.dsip.propampoldakalbar.rest_api.TransactionEndPoint;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComentLapGiatDialogFragment extends DialogFragment {

    public static final String ARG_PARAM_LAP_GIAT = "param_lap_giat";
    public static final String ARG_PARAM_USER = "param_user";

    private OnComentPostedListener mListener;
    private ProgressDialog progress;
    private LapGiat mLapGiat;
    private UserSession user;
    private List<Coment> comments = new ArrayList<Coment>();
    private KomentarAdapter mAdapter;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.etComment) EditText etComment;
    @BindView(R.id.ibSend) ImageButton ibSend;
    @BindView(R.id.btClose) Button btClose;

    public static ComentLapGiatDialogFragment newInstance(LapGiat lapGiat, UserSession user) {
        ComentLapGiatDialogFragment fragment = new ComentLapGiatDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_LAP_GIAT, Parcels.wrap(lapGiat));
        args.putParcelable(ARG_PARAM_USER, Parcels.wrap(user));
        fragment.setArguments(args);
        return fragment;
    }

    public ComentLapGiatDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coment, container, false);

        ButterKnife.bind(this, view);

        Dialog dialog = getDialog();
        dialog.setTitle("List Komentar");
        dialog.setCancelable(false);

        if (getArguments() != null) {
            user = (UserSession) Parcels.unwrap(getArguments().getParcelable(ARG_PARAM_USER));

            mLapGiat = (LapGiat) Parcels.unwrap(getArguments().getParcelable(ARG_PARAM_LAP_GIAT));
            List<Coment> com = mLapGiat.trnLapGiatComents;
            comments.addAll(com);
        }

        mAdapter = new KomentarAdapter(getActivity(), comments);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comentText = etComment.getText().toString();
                if (TextUtils.isEmpty(comentText)) {
                    //Toast.makeText(getActivity(), "Komentar kosong", Toast.LENGTH_LONG).show();
                }else{
                    postComent(comentText);
                }
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void postComent(String coment){
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Mengirim data");
        progress.setMessage("Mohon tunggu......");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.show();

        Map<String,String> params = new HashMap<String, String>();
        params.put("TrnLapGiatComent[lap_giat_id]", mLapGiat.id);
        params.put("TrnLapGiatComent[coment]", coment);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + user.user.auth_key);

        OkHttpClient httpClient = new MyOkHttpInterceptor(headers).getOkHttpClient();
        TransactionEndPoint service = ServiceGenerator.createService(TransactionEndPoint.class, httpClient);
        Call<Coment> call = service.createComentLapGiat(params);

        call.enqueue(new Callback<Coment>() {
            @Override
            public void onResponse(Call<Coment> call, Response<Coment> response) {
                progress.dismiss();

                Gson gson = new Gson();

                if(response.isSuccessful()){
                    Coment comentResponse = response.body();
                    comments.add(comentResponse);
                    mAdapter.notifyDataSetChanged();
                    etComment.setText("");

                    if (mListener != null) {
                        mListener.onComentPosted(comentResponse);
                    }
                }else{
                    String failureMessage = response.message();

                    try {
                        String errorBody = response.errorBody().string();

                        if(response.code() == 422){
                            Type errorType = new TypeToken<List<ErrorMessage>>(){}.getType();
                            List<ErrorMessage> errorMessages = gson.fromJson(errorBody, errorType);

                            String errMsg = failureMessage + "\n";
                            for(ErrorMessage err : errorMessages){
                                errMsg = errMsg.concat(err.field + ": " + err.message + "\n");
                            }
                            Toast.makeText(getActivity(), errMsg, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Coment> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnComentPostedListener {
        void onComentPosted(Coment coment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnComentPostedListener) {
            mListener = (OnComentPostedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
