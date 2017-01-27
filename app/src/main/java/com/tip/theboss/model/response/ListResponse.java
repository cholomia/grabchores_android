package com.tip.theboss.model.response;

/**
 * Created by Cholo Mia on 12/22/2016.
 */

public class ListResponse {

    private int count;
    private String next;
    private String previous;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }
}
