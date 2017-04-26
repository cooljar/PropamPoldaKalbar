package id.co.dsip.propampoldakalbar.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.model.UserSession;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInstruksi extends Fragment {

    private static final String ARG_PARAM_LAYOUT_WIDTH = "param_layout_width";
    private static final String ARG_PARAM_LAYOUT_HEIGHT = "param_layout_height";
    private static final String ARG_PARAM_USER_SESSION = "param_user_session";

    private UserSession user;
    private ProgressDialog progress;
    //private List<Instru> mBerita = new ArrayList<Berita>();
    //private BeritaAdapter mAdapter;
    private int pageCount = 0;
    private int layoutWidth, layoutHeight;

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

        return view;
    }
}
