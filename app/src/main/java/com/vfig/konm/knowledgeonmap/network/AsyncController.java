package com.vfig.konm.knowledgeonmap.network;

import android.content.Context;
import android.os.AsyncTask;

import com.vfig.konm.knowledgeonmap.parser.ParseWikiCoordinateResponse;
import com.vfig.konm.knowledgeonmap.utils.IRequestType;

import java.util.HashMap;

public class AsyncController<T> extends AsyncTask<String, Integer, T> {

    private static WebServiceHandlerInterface webServiceHandlerInterface;
    private Context mContext;
    private String url;
    private HashMap<String, String> hashMap;
    private String requestType;

    public AsyncController(Context mContext, String url, HashMap<String, String>
            hashMap, String requestType) {
        this.mContext = mContext;
        this.url = url;
        this.hashMap = hashMap;
        this.requestType = requestType;
    }

    public static void setWebServiceHandlerInterface(WebServiceHandlerInterface
                                                             webServiceHandlerInterface) {
        AsyncController.webServiceHandlerInterface = webServiceHandlerInterface;
    }

    private Context getmContext() {
        return mContext;
    }

    @Override
    protected T doInBackground(String... strings) {

        String response = WebServiceHandler.callApi(getmContext(), url, hashMap);

        switch (requestType) {
            case IRequestType._MAP:
                return (T) ParseWikiCoordinateResponse.getWikiPageDetails(response);
            case IRequestType._SEARCH:
                return (T) ParseWikiCoordinateResponse.getSearchDetail(response);
            default:
                return null;
        }
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        if (t==null){
            webServiceHandlerInterface.onError(t);
        }
        else {
            webServiceHandlerInterface.onSuccess(t);
        }


    }

    private void getState() {
        getStatus();

    }
}

