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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import id.co.dsip.propampoldakalbar.activity.LapGiatDetailActivity;
import id.co.dsip.propampoldakalbar.activity.TambahLapGiatActivity;
import id.co.dsip.propampoldakalbar.adapter.LapGiatAdapter;
import id.co.dsip.propampoldakalbar.adapter.LapInfoAdapter;
import id.co.dsip.propampoldakalbar.helpers.EndlessRecyclerViewScrollListener;
import id.co.dsip.propampoldakalbar.helpers.OnItemClickListener;
import id.co.dsip.propampoldakalbar.model.LapGiat;
import id.co.dsip.propampoldakalbar.model.LapInfo;
import id.co.dsip.propampoldakalbar.model.UserSession;
import id.co.dsip.propampoldakalbar.rest_api.ErrorMessage;
import id.co.dsip.propampoldakalbar.rest_api.MyOkHttpInterceptor;
import id.co.dsip.propampoldakalbar.rest_api.ServiceGenerator;
import id.co.dsip.propampoldakalbar.rest_api.TransactionEndPoint;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLapGiat extends Fragment {

    private static final String ARG_PARAM_LAYOUT_WIDTH = "param_layout_width";
    private static final String ARG_PARAM_LAYOUT_HEIGHT = "param_layout_height";
    private static final String ARG_PARAM_USER_SESSION = "param_user_session";

    private UserSession user;
    private ProgressDialog progress;
    private List<LapGiat> mLapGiat = new ArrayList<LapGiat>();
    private LapGiatAdapter mAdapter;
    private int pageCount = 0;
    private int layoutWidth, layoutHeight;

    @BindView(R.id.swipeContainerLapGiat) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view_lap_giat) RecyclerView mRecyclerView;
    @BindView(R.id.llProgress) LinearLayout llProgress;
    @BindView(R.id.llContainer) LinearLayout llContainer;

    public static FragmentLapGiat newInstance(int layoutWidth, int layoutHeight, UserSession user) {

        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_LAYOUT_WIDTH, layoutWidth);
        args.putInt(ARG_PARAM_LAYOUT_HEIGHT, layoutHeight);
        args.putParcelable(ARG_PARAM_USER_SESSION, Parcels.wrap(user));

        FragmentLapGiat fragment = new FragmentLapGiat();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentLapGiat() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("LC", "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            layoutWidth = getArguments().getInt(ARG_PARAM_LAYOUT_WIDTH, 0);
            layoutHeight = getArguments().getInt(ARG_PARAM_LAYOUT_HEIGHT, 0);
            user = Parcels.unwrap(getArguments().getParcelable(ARG_PARAM_USER_SESSION));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("LC", "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.add){
            Bundle bundle = new Bundle();
            bundle.putParcelable(TambahLapGiatActivity.USER_PARCEL, Parcels.wrap(user));
            bundle.putInt(TambahLapGiatActivity.LAYOUT_WIDTH_PARCEL, layoutWidth);
            bundle.putInt(TambahLapGiatActivity.LAYOUT_HEIGHT_PARCEL, layoutHeight);

            Intent i = new Intent(getActivity(), TambahLapGiatActivity.class);
            i.putExtras(bundle);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_lap_giat, container, false);

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
                    getLapGiatData(page);
                }else Toast.makeText(getActivity(), "Tidak ada lagi data", Toast.LENGTH_SHORT).show();
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLapGiat.clear();
                mAdapter.notifyDataSetChanged();
                getLapGiatData(1);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mAdapter = new LapGiatAdapter(getActivity(), mLapGiat, layoutWidth);
        mAdapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LapGiat selectedLapGiat = mLapGiat.get(position);

                Bundle bundle = new Bundle();
                bundle.putParcelable(LapGiatDetailActivity.LAP_GIAT_PARCEL, Parcels.wrap(selectedLapGiat));
                bundle.putParcelable(LapGiatDetailActivity.USER_SESSION_PARCEL, Parcels.wrap(user));
                bundle.putInt(LapGiatDetailActivity.LAYOUT_WIDTH_PARCEL, layoutWidth);
                bundle.putInt(LapGiatDetailActivity.LAYOUT_HEIGHT_PARCEL, layoutHeight);

                Intent i = new Intent(getActivity(), LapGiatDetailActivity.class);
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

        getLapGiatData(1);

    }

    protected void getLapGiatData(int page){
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + user.user.auth_key);

        OkHttpClient httpClient = new MyOkHttpInterceptor(headers).getOkHttpClient();
        TransactionEndPoint service = ServiceGenerator.createService(TransactionEndPoint.class, httpClient);
        Call<JsonObject> call = service.getLapGiat(String.valueOf(page), "20");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(progress.isShowing()){
                    progress.dismiss();
                }

                if(swipeContainer.isRefreshing()){
                    swipeContainer.setRefreshing(false);
                }

                if(llProgress.getVisibility() == View.VISIBLE){
                    llProgress.setVisibility(View.GONE);
                }

                Gson gson = new Gson();

                if(response.isSuccessful()){
                    JsonArray responseData = response.body().getAsJsonArray("items");
                    Type itemsType = new TypeToken<List<LapGiat>>(){}.getType();
                    List<LapGiat> data = gson.fromJson(responseData, itemsType);
                    mLapGiat.addAll(data);

                    JsonObject meta = response.body().getAsJsonObject("_meta");
                    pageCount = meta.get("pageCount").getAsInt();

                    int curSize = mAdapter.getItemCount();
                    mAdapter.notifyItemRangeInserted(curSize, mLapGiat.size());
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

                if(swipeContainer.isRefreshing()){
                    swipeContainer.setRefreshing(false);
                }

                if(llProgress.getVisibility() == View.VISIBLE){
                    llProgress.setVisibility(View.GONE);
                }

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
