package com.android.collegeproject.model;

import java.util.List;

public class Sources{
    String status;
    List<Source> sources;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Source> getSource() {
        return sources;
    }

    public void setSource(List<Source> sources) {
        this.sources = sources;
    }
}
