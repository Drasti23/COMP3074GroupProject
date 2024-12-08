package ca.gbc.comp3074groupt22.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ca.gbc.comp3074groupt22.R;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_order_item, parent, false);
        }

        Order order = orderList.get(position);

        TextView orderNumberTextView = convertView.findViewById(R.id.orderNumber);
        TextView orderStatusTextView = convertView.findViewById(R.id.orderStatus);

        orderNumberTextView.setText(order.getOrderNumber());
        orderStatusTextView.setText(order.getOrderStatus());

        return convertView;
    }
}
