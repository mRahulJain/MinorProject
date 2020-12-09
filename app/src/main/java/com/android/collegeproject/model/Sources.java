package com.android.collegeproject.model;

import java.util.List;

public class Sources{
    String status;
    List<NewsModelClass.Source> sources;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NewsModelClass.Source> getSource() {
        return sources;
    }

    public void setSource(List<NewsModelClass.Source> sources) {
        this.sources = sources;
    }
}
