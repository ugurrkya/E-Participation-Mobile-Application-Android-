package com.ep.sb.e_participation;

public class Vote {
    private String date;
    private String userid;
    private String name;
    private String description;
    private String title;
    private long votednumber;
    private long yes;
    private long no;
    private long counter;

    public Vote(String date, String userid, String name, String description, String title, long votednumber, long yes, long no, long counter) {
        this.date = date;
        this.userid = userid;
        this.name = name;
        this.description = description;
        this.title = title;
        this.votednumber = votednumber;
        this.yes = yes;
        this.no = no;
        this.counter = counter;
    }

    public Vote() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getVotednumber() {
        return votednumber;
    }

    public void setVotednumber(long votednumber) {
        this.votednumber = votednumber;
    }

    public long getYes() {
        return yes;
    }

    public void setYes(long yes) {
        this.yes = yes;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }
}
