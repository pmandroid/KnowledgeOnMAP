package com.vfig.konm.knowledgeonmap.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.vfig.konm.knowledgeonmap.R;
import com.vfig.konm.knowledgeonmap.adapter.SearchAdapter;
import com.vfig.konm.knowledgeonmap.network.AsyncController;
import com.vfig.konm.knowledgeonmap.network.WebServiceHandlerInterface;
import com.vfig.konm.knowledgeonmap.parser.beans.SearchResultBean;
import com.vfig.konm.knowledgeonmap.utils.IRequestType;
import com.vfig.konm.knowledgeonmap.utils.NavigationUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, View
        .OnTouchListener, WebServiceHandlerInterface {

    private final Executor mSearchExecutor = Executors.newSingleThreadExecutor();
    @Nullable
    private AsyncTask<String, Integer, List<SearchResultBean>> mSearchTask = null;

    private SearchView mSearchView;
    private InputMethodManager mImm;
    private String queryString;

    private SearchAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progress_bar;

    private List<Object> searchResults = Collections.emptyList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupAdAtBottom();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(SearchActivity.this, new SearchAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View view, int position, Object item) {
                final String title = ((SearchResultBean) item).getTitle();
                if (title != null) {
//                    Uri uri = Uri.parse("https://en.m.wikipedia.org/wiki/" + title);
//                    startActivity(new Intent(Intent.ACTION_VIEW, uri));

                    NavigationUtils.openWebPage(SearchActivity.this,title);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        AsyncController.setWebServiceHandlerInterface(SearchActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem menu_search = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) menu_search.getActionView();

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.search_library));

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);

        menu_search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        finish();
                        return false;
                    }
                });

        menu_search.expandActionView();

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void hideInputManager() {
        if (mSearchView != null) {
            if (mImm != null) {
                mImm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            }
            mSearchView.clearFocus();

//            SearchHistory.getInstance(this).addSearchString(queryString);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        onQueryTextChange(query);
        hideInputManager();

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.equals(queryString)) {
            return true;
        }
        if (mSearchTask != null) {
            mSearchTask.cancel(false);
            mSearchTask = null;
        }
        queryString = newText;
        if (queryString.trim().equals("")) {
            searchResults.clear();
            if(progress_bar!=null)
                progress_bar.setVisibility(View.GONE);
            //   adapter.updateSearchResults(searchResults);
            adapter.notifyDataSetChanged();
        } else {

            String wiki = "https://en.wikipedia.org/w/api";
            String search = ".php?action=query&srlimit=50&list=search&srsearch=";
            String format = "&utf8=&format=json";
            String url = wiki +
                    search + queryString +
                    format;

            progress_bar.setVisibility(View.VISIBLE);
            HashMap<String, String> hashMap = new HashMap<>();
            mSearchTask = new AsyncController<List<SearchResultBean>>(SearchActivity.this, url,
                    hashMap, IRequestType._SEARCH)
                    .executeOnExecutor(mSearchExecutor);
        }

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideInputManager();
        if(progress_bar!=null)
            progress_bar.setVisibility(View.GONE);
        return false;
    }


    @Override
    public void onSuccess(Object o) {
        if(progress_bar!=null)
        progress_bar.setVisibility(View.GONE);
        //    ProgressDialogue.hideProgressDialog();
        adapter.setList((List<SearchResultBean>) o);

        adapter.notifyDataSetChanged();


    }

    @Override
    public void onError(Object o) {
        if(progress_bar!=null)
        progress_bar.setVisibility(View.GONE);
        //   ProgressDialogue.hideProgressDialog();

    }

    @Override
    protected void onDestroy() {
        if (mSearchTask != null && mSearchTask.getStatus() != AsyncTask.Status.FINISHED) {
            mSearchTask.cancel(true);
        }
        super.onDestroy();
    }
}
