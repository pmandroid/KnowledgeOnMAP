package com.vfig.konm.knowledgeonmap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vfig.konm.knowledgeonmap.R;
import com.vfig.konm.knowledgeonmap.adapter.genricadapter.GenericRecycleViewAdapter;
import com.vfig.konm.knowledgeonmap.parser.beans.WikiPageDetail;
import com.vfig.konm.knowledgeonmap.parser.beans.prototypebean.Terms;
import com.vfig.konm.knowledgeonmap.parser.beans.prototypebean.Thumbnail;

public class WikiAdapter extends GenericRecycleViewAdapter<WikiPageDetail> {


    public WikiAdapter(Context context, OnViewHolderClick<WikiPageDetail> listener) {
        super(context, listener);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_adapter_view_main, viewGroup, false);

        return view;
    }

    @Override
    protected void bindView(WikiPageDetail item, int position, GenericRecycleViewAdapter
            .ViewHolder viewHolder) {
        if (item != null) {


            TextView name = (TextView) viewHolder.getView(R.id.tv_name);
            String title = item.getTitle();

            TextView tv_description = (TextView) viewHolder.getView(R.id.tv_description);
            name.setText(title);
            final Terms terms = item.getTerms();
            if (terms != null) {
                final String description = terms.getDescription();
                tv_description.setText(description);
            } else {
                tv_description.setText(title);
            }


            ImageView iv_thumbnail = (ImageView) viewHolder.getView(R.id.iv_thumbnail);


            final Thumbnail thumbnail = item.getThumbnail();
            if (thumbnail != null) {
                final String source = thumbnail.getSource();
                Glide.with(getContext()).load(source)
                        .thumbnail(0.5f)
                        .into(iv_thumbnail);
            }
        }


    }
}
