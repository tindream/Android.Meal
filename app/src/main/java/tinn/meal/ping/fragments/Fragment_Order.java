package tinn.meal.ping.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tinn.meal.ping.R;
import tinn.meal.ping.support.Config;

public class Fragment_Order extends Fragment_Base implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_order, container, false);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.load(R.id.order_context, R.id.order_load, R.id.order_text, false);
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        TextView textView = getActivity().findViewById(R.id.order_id);
        textView.setText("Order");
        super.load(R.id.order_context, R.id.order_load, R.id.order_text, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
