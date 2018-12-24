package com.vfig.konm.knowledgeonmap.parser.beans;

import com.vfig.konm.knowledgeonmap.parser.beans.prototypebean.CoordinatesItem;
import com.vfig.konm.knowledgeonmap.parser.beans.prototypebean.Terms;
import com.vfig.konm.knowledgeonmap.parser.beans.prototypebean.Thumbnail;

import java.util.List;

public class WikiPageDetail {

    private Thumbnail thumbnail;
    private int ns;
    private Terms terms;
    private List<CoordinatesItem> coordinates;
    private int index;
    private int pageid;
    private String title;

    public void setThumbnail(Thumbnail thumbnail){
        this.thumbnail = thumbnail;
    }

    public Thumbnail getThumbnail(){
        return thumbnail;
    }

    public void setNs(int ns){
        this.ns = ns;
    }

    public int getNs(){
        return ns;
    }

    public void setTerms(Terms terms){
        this.terms = terms;
    }

    public Terms getTerms(){
        return terms;
    }

    public void setCoordinates(List<CoordinatesItem> coordinates){
        this.coordinates = coordinates;
    }

    public List<CoordinatesItem> getCoordinates(){
        return coordinates;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }

    public void setPageid(int pageid){
        this.pageid = pageid;
    }

    public int getPageid(){
        return pageid;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    @Override
    public String toString(){
        return
                "WikiPageDetail{" +
                        "thumbnail = '" + thumbnail + '\'' +
                        ",ns = '" + ns + '\'' +
                        ",terms = '" + terms + '\'' +
                        ",coordinates = '" + coordinates + '\'' +
                        ",index = '" + index + '\'' +
                        ",pageid = '" + pageid + '\'' +
                        ",title = '" + title + '\'' +
                        "}";
    }
}
