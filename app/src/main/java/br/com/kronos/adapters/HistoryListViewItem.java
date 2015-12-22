package br.com.kronos.adapters;

public class HistoryListViewItem {
    private String activityName;
    private double quality;
    private double duration;


    public HistoryListViewItem(String activityName, double quality, double duration) {
        this.activityName = activityName;
        this.quality = quality;
        this.duration = duration;
    }
    public String getActivityName(){
        return activityName;
    }

    public double getQuality() {
        return quality;
    }

    public double getDuration(){
        return duration;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setQuality(double quality){
        this.quality = quality;
    }

    public void setDuration(double duration){
        this.duration = duration;
    }
}
