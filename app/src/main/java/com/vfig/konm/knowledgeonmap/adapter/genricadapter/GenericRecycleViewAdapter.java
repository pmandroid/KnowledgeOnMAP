package com.vfig.konm.knowledgeonmap.adapter.genricadapter;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//https://gist.github.com/Plumillon/f85c6be94e2fdaf339b9
public abstract class GenericRecycleViewAdapter<T> extends RecyclerView
        .Adapter<GenericRecycleViewAdapter.ViewHolder> {

    private List<T> items;
    private Context context;
    private OnViewHolderClick<T> listener;

    public GenericRecycleViewAdapter(Context context) {
        this(context, null);
    }


    public GenericRecycleViewAdapter(Context context, OnViewHolderClick<T> listener) {
        super();
        this.context = context;
        this.listener = listener;
        items = new ArrayList<>();
    }

    protected abstract View createView(Context context, ViewGroup viewGroup, int viewType);

    protected abstract void bindView(T item, int position, GenericRecycleViewAdapter.ViewHolder
            viewHolder);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(createView(context, viewGroup, viewType), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericRecycleViewAdapter.ViewHolder holder, int
            position) {
        bindView(getItem(position), position, holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(int index) {
        return ((items != null && index < items.size()) ? items.get(index) : null);
    }

    public Context getContext() {
        return context;
    }

    public List<T> getList() {
        return items;
    }

    public void setList(List<T> list) {
        items = list;
    }

    public void setClickListener(OnViewHolderClick listener) {
        this.listener = listener;
    }

    public void addAll(List<T> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void reset() {
        items.clear();
        notifyDataSetChanged();
    }

    public interface OnViewHolderClick<T> {
        void onClick(View view, int position, T item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Map<Integer, View> views;

        public ViewHolder(View view, OnViewHolderClick listener) {


            super(view);
            views = new HashMap<>();
            views.put(0, view);

            if (listener != null)
                view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null)
                listener.onClick(view, getAdapterPosition(), getItem(getAdapterPosition()));

        }

        public void initViewList(int[] idList) {
            for (int id : idList)
                initViewById(id);
        }

        public void initViewById(int id) {
            View view = (getView() != null ? getView().findViewById(id) : null);

            if (view != null)
                views.put(id, view);
        }

        public View getView() {
            return getView(0);
        }

        public View getView(int id) {
            if (views.containsKey(id))
                return views.get(id);
            else
                initViewById(id);

            return views.get(id);
        }
    }

}
