package id.co.dsip.propampoldakalbar.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.parceler.Parcels;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.activity.InstruksiDetailActivity;
import id.co.dsip.propampoldakalbar.adapter.InstruksiAdapter;
import id.co.dsip.propampoldakalbar.helpers.EndlessRecyclerViewScrollListener;
import id.co.dsip.propampoldakalbar.helpers.OnItemClickListener;
import id.co.dsip.propampoldakalbar.model.Instruksi;
import id.co.dsip.propampoldakalbar.model.UserSession;
import id.co.dsip.propampoldakalbar.rest_api.ErrorMessage;
import id.co.dsip.propampoldakalbar.rest_api.MyOkHttpInterceptor;
import id.co.dsip.propampoldakalbar.rest_api.ServiceGenerator;
import id.co.dsip.propampoldakalbar.rest_api.TransactionEndPoint;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.dsip.propampoldakalbar.R.id.llProgress;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInstruksi extends Fragment {

    private static final String ARG_PARAM_LAYOUT_WIDTH = "param_layout_width";
    private static final String ARG_PARAM_LAYOUT_HEIGHT = "param_layout_height";
    private static final String ARG_PARAM_USER_SESSION = "param_user_session";

    private UserSession user;
    private ProgressDialog progress;
    private List<Instruksi> mInstruksi = new ArrayList<Instruksi>();
    private InstruksiAdapter mAdapter;
    private int pageCount = 0;
    private int layoutWidth, layoutHeight;

    @BindView(R.id.recycler_view_rekan) RecyclerView mRecyclerView;
    @BindView(R.id.llProgress) LinearLayout llProgress;

    public static FragmentInstruksi newInstance(int layoutWidth, int layoutHeight, UserSession user) {

        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_LAYOUT_WIDTH, layoutWidth);
        args.putInt(ARG_PARAM_LAYOUT_HEIGHT, layoutHeight);
        args.putParcelable(ARG_PARAM_USER_SESSION, Parcels.wrap(user));

        FragmentInstruksi fragment = new FragmentInstruksi();
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentInstruksi newInstanceWithNotif(int layoutWidth, int layoutHeight, UserSession user, Bundle b) {

        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_LAYOUT_WIDTH, layoutWidth);
        args.putInt(ARG_PARAM_LAYOUT_HEIGHT, layoutHeight);
        args.putParcelable(ARG_PARAM_USER_SESSION, Parcels.wrap(user));

        FragmentInstruksi fragment = new FragmentInstruksi();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentInstruksi() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("LC", "onCreate");
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

        if (getArguments() != null) {
            layoutWidth = getArguments().getInt(ARG_PARAM_LAYOUT_WIDTH, 0);
            layoutHeight = getArguments().getInt(ARG_PARAM_LAYOUT_HEIGHT, 0);
            user = (UserSession) Parcels.unwrap(getArguments().getParcelable(ARG_PARAM_USER_SESSION));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_instruksi, container, false);

        ButterKnife.bind(this, view);

        llProgress.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager){
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(page <= pageCount){
                    llProgress.setVisibility(View.VISIBLE);
                    getInstruksiData(page, "");
                }else Toast.makeText(getActivity(), "Tidak ada lagi data", Toast.LENGTH_SHORT).show();
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);

        mAdapter = new InstruksiAdapter(getActivity(), mInstruksi, layoutWidth, layoutHeight);
        mAdapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Instruksi selectedInstruksi = mInstruksi.get(position);

                Bundle bundle = new Bundle();
                bundle.putParcelable(InstruksiDetailActivity.INSTRUKSI_PARCEL, Parcels.wrap(selectedInstruksi));
                bundle.putParcelable(InstruksiDetailActivity.USER_SESSION_PARCEL, Parcels.wrap(user));
                bundle.putInt(InstruksiDetailActivity.LAYOUT_WIDTH_PARCEL, layoutWidth);
                bundle.putInt(InstruksiDetailActivity.LAYOUT_HEIGHT_PARCEL, layoutHeight);

                Intent i = new Intent(getActivity(), InstruksiDetailActivity.class);
                i.putExtras(bundle);

                startActivity(i);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progress = new ProgressDialog(getActivity());
        progress.setTitle("Memuat data");
        progress.setMessage("Mohon tunggu......");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.show();

        getInstruksiData(1, "");

    }

    protected void getInstruksiData(int page, String queryNama){
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + user.user.auth_key);

        OkHttpClient httpClient = new MyOkHttpInterceptor(headers).getOkHttpClient();
        TransactionEndPoint service = ServiceGenerator.createService(TransactionEndPoint.class, httpClient);
        Call<JsonObject> call = service.getInstruksi(String.valueOf(page), "20");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(progress.isShowing()){
                    progress.dismiss();
                }

                if(llProgress.getVisibility() == View.VISIBLE){
                    llProgress.setVisibility(View.GONE);
                }

                Gson gson = new Gson();

                if(response.isSuccessful()){
                    JsonArray responseData = response.body().getAsJsonArray("items");
                    Type itemsType = new TypeToken<List<Instruksi>>(){}.getType();
                    List<Instruksi> data = gson.fromJson(responseData, itemsType);
                    mInstruksi.addAll(data);

                    JsonObject meta = response.body().getAsJsonObject("_meta");
                    pageCount = meta.get("pageCount").getAsInt();

                    int curSize = mAdapter.getItemCount();
                    mAdapter.notifyItemRangeInserted(curSize, mInstruksi.size());
                    mAdapter.notifyDataSetChanged();
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
                            Toast.makeText(getActivity(), errorBody, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if(progress.isShowing()){
                    progress.dismiss();
                }

                if(llProgress.getVisibility() == View.VISIBLE){
                    llProgress.setVisibility(View.GONE);
                }

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
