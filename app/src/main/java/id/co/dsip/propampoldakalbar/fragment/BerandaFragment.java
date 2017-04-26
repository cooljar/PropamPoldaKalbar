package id.co.dsip.propampoldakalbar.fragment;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.dsip.propampoldakalbar.R;
import im.delight.android.webview.AdvancedWebView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BerandaFragment extends Fragment implements AdvancedWebView.Listener{

    private AdvancedWebView mWebView;
    private ProgressDialog progress;

    public BerandaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        progress = new ProgressDialog(getActivity());

        progress.setTitle("Memuat data.");
        progress.setMessage("Mohon tunggu......");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCancelable(false);

        mWebView = (AdvancedWebView) view.findViewById(R.id.webview);
        mWebView.setListener(getActivity(), this);
        mWebView.loadUrl("http://e-topan.com/propam_kalbar/backend/web/index.php?r=informasi%2Findex-mobile");

        return view;
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        progress.show();
    }

    @Override
    public void onPageFinished(String url) {
        progress.dismiss();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}
