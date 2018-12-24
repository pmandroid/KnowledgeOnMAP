package com.vfig.konm.knowledgeonmap.network;

public interface WebServiceHandlerInterface<T> {

    void onSuccess(T t);

    void onError(T t);
}
