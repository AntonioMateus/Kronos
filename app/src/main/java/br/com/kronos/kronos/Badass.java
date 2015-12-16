package br.com.kronos.kronos;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.kronos.adapters.HistoryAdapterListView;
import br.com.kronos.kronos.adapters.HistoryListViewItem;

/**
 * Created by gustavo on 05/12/15.
 */
public class Badass {

    public KronosDatabase kronosDatabase;
    public List<Atividade> cumulativeActivityList;
    public HistoryAdapterListView historyAdapterListView;
    public List<HistoryListViewItem> itens;
    public Context context;
    public ListView listView;


    Date today = new java.util.Date();
    long oneDay = 86400000l; //1 day in milliseconds
    long oneWeek = 604800000l; //1 week in milliseconds
    long oneMonth = 2592000000l; //1 month in milliseconds
    long oneYear = 315360000000000l; //1 year in milliseconds

    long diff;
    Date searchDate;
    DateFormat df = new SimpleDateFormat("ddMMyyyy");
    String date;


    public Badass(Context context,List<Atividade> cumulativeActivityList, KronosDatabase kronosDatabase,HistoryAdapterListView historyAdapterListView,ListView listView){
        this.cumulativeActivityList = cumulativeActivityList;
        this.kronosDatabase = kronosDatabase;
        this.historyAdapterListView = historyAdapterListView;
        this.context = context;
        this.listView = listView;
        itens = new ArrayList<>();
    }

    public void selectedTime(String s){
        long forLimit = 0;
        if(s.equalsIgnoreCase("yesterday")){
            forLimit = oneDay;
        }
        else if(s.equalsIgnoreCase("last week")){
            forLimit = oneWeek;
        }
        else if(s.equalsIgnoreCase("last month")){
            forLimit = oneMonth;
        }
        else if(s.equalsIgnoreCase("last year")){
            forLimit = oneDay*365;
        }
        //this temporary list stores the activities of a day
        List<Atividade> tempActivityList;

        //clean the memory
        cumulativeActivityList.removeAll(cumulativeActivityList);
        //days that the user used the app, useful for showing the days of use
        //^not done yet, might create a class for it
        //number of days that the app was used
        int totalDays = 0;
        int cont = 0;

        //for that goes day by day
        for (long daysIterator = oneDay; daysIterator <= forLimit; daysIterator += oneDay) {
            diff = today.getTime() - daysIterator;
            searchDate = new Date(diff);
            date = df.format(searchDate);
            int day = Integer.parseInt(date.substring(0, 2));
            int month = Integer.parseInt(date.substring(2, 4));
            int year = Integer.parseInt(date.substring(4, 8));
            //gets the list of a specific day
            tempActivityList = kronosDatabase.getAtividadesHistorico(day, month, year);

            //if the list is not empty means that the app was used
            if (tempActivityList.size() > 0) {
                totalDays = totalDays + 1;
                //iterates on the activities of a day
                for (Atividade activities : tempActivityList) {
                    //if its new stores in a list called cumulative
                    if (!cumulativeActivityList.contains(activities)) {
                        double duration = activities.getDuracao();
                        double quality = activities.getQualidade();
                        String name = activities.getNome();
                        Atividade newActivity = new Atividade(name, duration, quality, day, month, year);
                        cumulativeActivityList.add(newActivity);
                    } else {
                        //if already exists, acummulate
                        int index = cumulativeActivityList.indexOf(activities);
                        Atividade cumuAct = cumulativeActivityList.get(index);
                        double cumuDuration = cumuAct.getDuracao();
                        double cumuQuality = cumuAct.getQualidade();
                        double tempDuration = activities.getDuracao();
                        double tempQuality = activities.getQualidade();
                        cumuAct.setDuracao(cumuDuration + tempDuration);
                        cumuAct.setQualidade(cumuQuality + tempQuality);
                    }
                }
            }
        }
        //divide the result by the number of the days that the app was used
        for(Atividade cumuActivities : cumulativeActivityList){
            double changeDuration = cumuActivities.getDuracao()/totalDays;
            double changeQuality = cumuActivities.getQualidade()/totalDays;
            cumuActivities.setDuracao(changeDuration);
            cumuActivities.setQualidade(changeQuality);
            //and add it to the adapter so that we can expand the list
            HistoryListViewItem listItem = new HistoryListViewItem(cumuActivities.getNome(),cumuActivities.getQualidade(),cumuActivities.getDuracao());
            itens.add(listItem);
        }
    }

    public void setTheMiracle(){
        HistoryAdapterListView test = new HistoryAdapterListView(context,itens);
        listView.setAdapter(test);//Alimentar a lista de atividades com o Adapter
        listView.setSelection(itens.size() - 1);
    }
}
