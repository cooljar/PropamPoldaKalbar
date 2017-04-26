package id.co.dsip.propampoldakalbar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.helpers.FitXyTransformation;
import id.co.dsip.propampoldakalbar.model.AttachmentGridItem;

/**
 * Created by japra_awok on 14/04/2017.
 */

public class AttachmentItemAdapter extends BaseAdapter{

    private List<AttachmentGridItem> attachmentList;
    private Context context;
    private static LayoutInflater inflater = null;
    private int layoutWidth;

    public AttachmentItemAdapter(Context context, List<AttachmentGridItem> attachmentList, int layoutWidth) {
        this.layoutWidth = layoutWidth;
        this.attachmentList = attachmentList;
        this.context = context;
        inflater = ( LayoutInflater )this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return attachmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.content_attachment_grid_item, null);

        AttachmentGridItem att = attachmentList.get(position);

        String attType = att.mime_type;
        int slashIndex = attType.indexOf("/");
        String fileType = attType.substring(0, slashIndex);

        holder.iv = (ImageView) rowView.findViewById(R.id.iv);

        switch (fileType){
            case "image":
                Picasso.with(context)
                        .load("file://" + att.thumb_path)
                        //.resize(150, 150)
                        //.centerCrop()
                        .transform(new FitXyTransformation(layoutWidth / 4, true))
                        .placeholder(R.drawable.loading) // can also be a drawable
                        .into(holder.iv)
                ;
                break;
            case "video":
                Picasso.with(context)
                        .load("file://" + att.thumb_path)
                        //.resize(150, 150)
                        //.centerCrop()
                        .transform(new FitXyTransformation(layoutWidth / 4, true))
                        .placeholder(R.drawable.loading) // can also be a drawable
                        .into(holder.iv)
                ;
                break;
        }

        final int pos = position;
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, attachmentList.get(pos).file_name, Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
    }

    public class Holder
    {
        ImageView iv;
    }
}
