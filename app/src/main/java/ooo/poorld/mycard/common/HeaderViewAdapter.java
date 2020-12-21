package ooo.poorld.mycard.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.entity.CardImage;

/**
 * author: teenyda
 * date: 2020/12/21
 * description:
 */
public class HeaderViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0; //说明是带有Header的
    public static final int TYPE_FOOTER = 1; //说明是带有Footer的
    public static final int TYPE_NORMAL = 2; //说明是不带有header和footer的

    //获取从Activity中传递过来每个item的数据集合
    private List<CardImage> mDatas;
    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;
    private Context mContext;
    private HeaderClickListener mHeaderClickListener;
    private OnItemClickListener mOnItemClickListener;

    //构造函数
    public HeaderViewAdapter(Context context){
        this.mContext = context;
        this.mDatas = new ArrayList<>();
    }

    public void addDatas(List<CardImage> medias) {
        mDatas.addAll(medias);
        notifyDataSetChanged();
    }

    public void setHeaderClickListener(HeaderClickListener headerClickListener) {
        mHeaderClickListener = headerClickListener;
    }
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }

    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view * */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null){
            return TYPE_NORMAL;
        }
        if (position == 0 && mHeaderView != null){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount()-1 && mFooterView != null){
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ListHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new ListHolder(mFooterView);
        }
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_cert_image, parent, false);
        return new ListHolder(layout);
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的， HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof ListHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                CardImage localMedia = mDatas.get(position - 1);
                String compressPath = localMedia.getFilePath();
                Bitmap bitmap = BitmapFactory.decodeFile(compressPath);
                ((ListHolder) holder).item_cert_image.setImageBitmap(bitmap);
                if (mOnItemClickListener != null) {
                    ((ListHolder) holder).item_cert_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnItemClickListener.onItemClick(localMedia);
                        }
                    });

                    ((ListHolder) holder).item_cert_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDatas.remove(position - 1);
                            mOnItemClickListener.onRemoveClick(localMedia);
                            notifyDataSetChanged();
                        }
                    });
                }

                return;
            }
            return;
        }else if(getItemViewType(position) == TYPE_HEADER){
            if (mHeaderView != null && mHeaderClickListener != null) {
                mHeaderView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mHeaderClickListener.onHeaderClick();
                    }
                });
            }
            return;
        }else{
            return;
        }
    }

    //在这里面加载ListView中的每个item的布局
    class ListHolder extends RecyclerView.ViewHolder{
        ImageView item_cert_remove;
        ImageView item_cert_image;
        public ListHolder(View itemView) {
            super(itemView);
            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView){
                return;
            }
            if (itemView == mFooterView){
                return;
            }
            item_cert_remove = (ImageView)itemView.findViewById(R.id.item_cert_remove);
            item_cert_image = (ImageView)itemView.findViewById(R.id.item_cert_image);
        }
    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null){
            return mDatas.size();
        }else if(mHeaderView == null && mFooterView != null){
            return mDatas.size() + 1;
        }else if (mHeaderView != null && mFooterView == null){
            return mDatas.size() + 1;
        }else {
            return mDatas.size() + 2;
        }
    }

    public interface HeaderClickListener {
        void onHeaderClick();
    }

    public interface OnItemClickListener {
        void onItemClick(CardImage localMedia);

        void onRemoveClick(CardImage localMedia);
    }
}
