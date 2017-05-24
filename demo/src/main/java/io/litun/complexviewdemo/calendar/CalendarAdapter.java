package io.litun.complexviewdemo.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.litun.complexview.ComplexView;
import io.litun.complexviewdemo.R;

/**
 * Created by Litun on 24.05.2017.
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<Month> months = new ArrayList<>();

    public CalendarAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setMonths(List<Month> months) {
        this.months = months;
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CalendarViewHolder(
                layoutInflater.inflate(R.layout.item_calendar_month, parent, false));
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, int position) {
        Month month = months.get(position);
        holder.name.setText(month.getName());
        holder.complexView.setData(month.getComplexViewModel());
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.month_name)
        TextView name;
        @BindView(R.id.month_complex_view)
        ComplexView complexView;

        public CalendarViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
