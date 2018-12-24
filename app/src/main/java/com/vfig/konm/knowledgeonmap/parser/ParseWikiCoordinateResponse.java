package com.vfig.konm.knowledgeonmap.parser;

import com.vfig.konm.knowledgeonmap.parser.beans.SearchResultBean;
import com.vfig.konm.knowledgeonmap.parser.beans.WikiPageDetail;
import com.vfig.konm.knowledgeonmap.parser.beans.prototypebean.CoordinatesItem;
import com.vfig.konm.knowledgeonmap.parser.beans.prototypebean.Terms;
import com.vfig.konm.knowledgeonmap.parser.beans.prototypebean.Thumbnail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ParseWikiCoordinateResponse {


    public static List<WikiPageDetail> getWikiPageDetails(String response) {
        try {
            return parseResponseWIKI(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<SearchResultBean> getSearchDetail(String response){
        try {
            return parseSearchResponse(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static List<WikiPageDetail> parseResponseWIKI(String response) throws JSONException {
        List<WikiPageDetail> arrayListWikiPageDetail = new ArrayList<>();
        if (response != null && !response.equalsIgnoreCase("")) {
            if (response.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("query")) {
                    JSONObject jsonObjectQuery = jsonObject.optJSONObject("query");
                    if (jsonObjectQuery.has("pages")) {
                        JSONObject jsonObjectPages = jsonObjectQuery.optJSONObject("pages");

                        Iterator<?> keys = jsonObjectPages.keys();

                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            if (jsonObjectPages.get(key) instanceof JSONObject) {
                                JSONObject jsonObjectPageDetail = (JSONObject) jsonObjectPages
                                        .get(key);
                                WikiPageDetail wikiPageDetail = new WikiPageDetail();

                                wikiPageDetail.setIndex(jsonObjectPageDetail.optInt("index"));
                                wikiPageDetail.setPageid(jsonObjectPageDetail.optInt("pageid"));
                                wikiPageDetail.setTitle(jsonObjectPageDetail.optString("title"));


                                //TODO add terms....
                                if (jsonObjectPageDetail.has("terms")) {
                                    JSONObject jsonObjectTerms = jsonObjectPageDetail
                                            .optJSONObject("terms");


                                    JSONArray jsonArrayDescription = jsonObjectTerms.optJSONArray
                                            ("description");


                                    String description = jsonArrayDescription.optString(0);


                                    Terms terms = new Terms();
                                    terms.setDescription(description);
                                    wikiPageDetail.setTerms(terms);
                                }

                                //TODO add coordinates....

                                if (jsonObjectPageDetail.has("coordinates")) {
                                    JSONArray jACoordinates = jsonObjectPageDetail.optJSONArray
                                            ("coordinates");


                                    JSONObject jOCoordinates = jACoordinates.optJSONObject(0);


                                    CoordinatesItem coordinatesItem = new CoordinatesItem();
                                    coordinatesItem.setLat(jOCoordinates.optDouble("lat"));
                                    coordinatesItem.setLon(jOCoordinates.optDouble("lon"));
                                    List<CoordinatesItem> coordinatesList = new ArrayList<>();
                                    coordinatesList.add(coordinatesItem);
                                    wikiPageDetail.setCoordinates(coordinatesList);
                                }

                                //TODO add thumbnail...

                                if (jsonObjectPageDetail.has("thumbnail")) {

                                    JSONObject jOThumbnail = jsonObjectPageDetail.optJSONObject
                                            ("thumbnail");


                                    Thumbnail thumbnail = new Thumbnail();
                                    if (jOThumbnail.has("source")) {
                                        thumbnail.setSource(jOThumbnail.optString("source"));
                                        thumbnail.setHeight(jOThumbnail.getInt("height"));
                                        thumbnail.setWidth(jOThumbnail.getInt("width"));
                                        wikiPageDetail.setThumbnail(thumbnail);
                                    }
                                }

                                arrayListWikiPageDetail.add(wikiPageDetail);
                            }
                        }
                    }

                } else {
                    arrayListWikiPageDetail = Collections.emptyList();
                }

            }

        }
        return arrayListWikiPageDetail;

    }


    private static List<SearchResultBean> parseSearchResponse(String response) throws
            JSONException {
        List<SearchResultBean> aLSearchResult = new ArrayList<>();
        if (response != null && !response.equalsIgnoreCase("")) {

            if (response.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("query")) {
                    JSONObject jsonObjectQuery = jsonObject.optJSONObject("query");
                    if (jsonObjectQuery.has("search")){
                        JSONArray jsonArray = jsonObjectQuery.optJSONArray("search");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            SearchResultBean searchResultBean = new SearchResultBean();
                            JSONObject jsonObjectData =jsonArray.getJSONObject(i);
                            String title = jsonObjectData.optString("title");
                            String snippet = jsonObjectData.optString("snippet");
                            searchResultBean.setTitle(title);
                            searchResultBean.setSnippet(snippet+"...");
                            aLSearchResult.add(searchResultBean);

                        }
                    }
                }
            }
        }
        return aLSearchResult;
    }

}
