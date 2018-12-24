package com.vfig.konm.knowledgeonmap.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vfig.konm.knowledgeonmap.R;
import com.vfig.konm.knowledgeonmap.adapter.genricadapter.GenericRecycleViewAdapter;
import com.vfig.konm.knowledgeonmap.parser.beans.SearchResultBean;

public class SearchAdapter extends GenericRecycleViewAdapter<SearchResultBean> {


    public SearchAdapter(Context context, OnViewHolderClick<SearchResultBean> listener) {
        super(context, listener);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);

        return inflater.inflate(R.layout.list_adapter_search_view, viewGroup, false);
    }

    @Override
    protected void bindView(SearchResultBean item, int position, GenericRecycleViewAdapter
            .ViewHolder viewHolder) {
        if (item != null) {


            TextView tv_name = (TextView) viewHolder.getView(R.id.tv_name);
            TextView tv_description = (TextView) viewHolder.getView(R.id.tv_description);
            String snippet = item.getSnippet();
            String title = item.getTitle();
            tv_name.setText(title);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tv_description.setText(Html.fromHtml(snippet, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tv_description.setText(Html.fromHtml(snippet));
            }
        }


    }
}
