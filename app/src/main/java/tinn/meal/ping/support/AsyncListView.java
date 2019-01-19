package tinn.meal.ping.support;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.R;
import tinn.meal.ping.enums.IListListener;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.info.HolderInfo;
import tinn.meal.ping.info.loadInfo.AdapterInfo;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.info.loadInfo.LoaderInfo;

public class AsyncListView extends AsyncBase {
    protected IListListener listListener;
    private Context context;

    protected <T> void onListener(ObservableEmitter<LoadInfo> emitter, ViewHolder holder, T object) {
        try {
            if (listListener != null)
                listListener.onReady(emitter, holder, object);
        } catch (Exception ex) {
            Method.log(ex);
        }
    }

    public AsyncListView setListener(IListListener listListener, ILoadListener loadListener) {
        this.listListener = listListener;
        this.loadListener = loadListener;
        return this;
    }

    public <T> void init(Context context, List<T> list, int resId) {
        this.context = context;
        new AsyncAdapter().setListener((emitter, info) -> {
            MyAdapter<T> myAdapter1 = new MyAdapter<T>(context, resId, list) {
                @Override
                public void bindView(ViewHolder holder, T obj) {
                    new AsyncAdapter().setListener((emitter, info) -> {
                        onListener(emitter, holder, obj);
                    }, obj2 -> {
                        onCallUI(obj2);
                    }).init();
                }
            };
            emitter.onNext(new AdapterInfo(myAdapter1, list));
        }, obj2 -> {
            onCallUI(obj2);
        }).init();
    }

    @Override
    protected void onCallUI(LoadInfo info) {
        switch (info.type) {
            case setText:
                HolderInfo holder = ((LoaderInfo) info).holderInfo;
                holder.viewHolder.setText(holder.id, holder.msg);
                break;
            case setImage:
                holder = ((LoaderInfo) info).holderInfo;
                ImageView imageView = holder.viewHolder.getView(holder.id);
                Picasso.with(context).load(holder.file).placeholder(R.drawable.ic_icon).into(imageView);
                break;
            case setImageId:
                holder = ((LoaderInfo) info).holderInfo;
                imageView = holder.viewHolder.getView(holder.id);
                imageView.setImageDrawable(Config.context.getDrawable(holder.imageId));
                if (!Method.isEmpty(holder.msg))
                    imageView.setBackground(Config.context.getDrawable(Integer.parseInt(holder.msg)));
                break;
            case setIcon:
                holder = ((LoaderInfo) info).holderInfo;
                imageView = holder.viewHolder.getView(holder.id);
                Picasso.with(context).load(R.drawable.ic_icon).placeholder(R.drawable.ic_icon).into(imageView);
                break;
        }
        super.onCallUI(info);
    }
}
