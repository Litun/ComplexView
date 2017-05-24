package io.litun.complexviewdemo.calendar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.litun.complexview.ComplexViewModel;
import io.litun.complexview.model.Markup2;
import io.litun.complexview.model.MarkupFrame2;
import io.litun.complexview.model.MarkupTextElement2;
import io.litun.complexview.model.ScaleMode;
import io.litun.complexviewdemo.R;

public class CalendarActivity extends AppCompatActivity {
    private final SimpleDateFormat monthFormatter = new SimpleDateFormat("MMMM");
    private CalendarAdapter calendarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        calendarAdapter = new CalendarAdapter(this);
        recycler.setAdapter(calendarAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        new AsyncTask<Void, Void, List<Month>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<Month> doInBackground(Void... params) {
                List<Month> months = generateMonths();
                return months;
            }

            @Override
            protected void onPostExecute(List<Month> months) {
                calendarAdapter.setMonths(months);
                calendarAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private List<Month> generateMonths() {
        Calendar startCalendar = GregorianCalendar.getInstance();
        Calendar finishCalendar = GregorianCalendar.getInstance();
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        finishCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
        startCalendar.add(Calendar.YEAR, -1);
        finishCalendar.add(Calendar.YEAR, +1);
        List<Month> result = new ArrayList<>(25);
        while (startCalendar.before(finishCalendar)) {
            Month month = generateMonth(startCalendar, finishCalendar);
            result.add(month);
            startCalendar.add(Calendar.MONTH, +1);
            startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }

    private Month generateMonth(Calendar startCalendar, Calendar finishCalendar) {
        String monthName = monthFormatter.format(startCalendar.getTime());
        Calendar monthCalendar = GregorianCalendar.getInstance();
        int firstDayOfWeek = monthCalendar.getFirstDayOfWeek();
        monthCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
        ComplexViewModel viewModel = new ComplexViewModel.Builder(this)
                .setMarkupProcessor((object, resourceCache) -> {
                    Markup2.Builder builder = new Markup2.Builder()
                            .setScaleMode(ScaleMode.INSCRIBE);
                    int line = 0;
                    while (monthCalendar.before(finishCalendar) &&
                            monthCalendar.get(Calendar.MONTH) == startCalendar.get(Calendar.MONTH)) {
                        int dayOfMonth = monthCalendar.get(Calendar.DAY_OF_MONTH);
                        int dayOfWeek = monthCalendar.get(Calendar.DAY_OF_WEEK);
                        int dayI = (dayOfWeek - firstDayOfWeek + 6) % 7;
                        builder.addFrame(new MarkupFrame2.Builder(dayI, line, 1, 1)
                                .addElement(new MarkupTextElement2(0.2f, 0.2f, 0.2f, 0.2f, 1,
                                        ScaleMode.INSCRIBE, String.valueOf(dayOfMonth)))
                                .build());

                        monthCalendar.add(Calendar.DAY_OF_YEAR, +1);
                        if (dayI == 6) {
                            line++;
                        }
                    }
                    return builder
                            .setSize(7, line + 1)
                            .build();
                })
                .build();
        return new Month(monthName, viewModel);
    }
}
