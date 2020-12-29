package ooo.poorld.mycard.model.cert.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import io.zhuliang.appchooser.AppChooser;
import ooo.poorld.mycard.BuildConfig;
import ooo.poorld.mycard.R;

/**
 * author: teenyda
 * date: 2020/12/21
 * description:
 */
public class CertImageAdapter extends RecyclerView.Adapter<CertImageAdapter.ViewHolder> {

    private Context mContext;

    private List<LocalMedia> mCardImages;

    private View mHeaderView;

    public static final int TYPE_HEADER = 0;

    public static final int TYPE_NORMAL = 1;

    private OnItemClickListener mListener;


    public CertImageAdapter(Context context) {
        this.mContext = context;
        mCardImages = new ArrayList<>();
    }

    public void addData(LocalMedia bitmap) {
        mCardImages.add(bitmap);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER)
            return new ViewHolder(mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cert_image, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;

        final int pos = getRealPosition(holder);
        LocalMedia localMedia = mCardImages.get(pos);
        if(holder instanceof ViewHolder) {
            // ((Holder) holder).text.setText(data);
            String compressPath = localMedia.getCompressPath();
            Bitmap bitmap = BitmapFactory.decodeFile(compressPath);
            holder.item_cert_image.setImageBitmap(bitmap);
            if(mListener == null) return;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // mListener.onItemClick(pos, data);
                    AppChooser.from((FragmentActivity) mContext)
                            .file(new File(compressPath))
                            .requestCode(20001)
                            .authority(BuildConfig.APPLICATION_ID + ".fileprovider")
                            .load();
                }
            });


        }

        /*Bitmap bitmap = mCardImages.get(position);
        holder.item_cert_image.*/
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mCardImages.size() : mCardImages.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_cert_image;
        ImageView item_cert_remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if(itemView == mHeaderView) return;
            item_cert_image = itemView.findViewById(R.id.item_cert_image);
            item_cert_remove = itemView.findViewById(R.id.item_cert_remove);
        }
    }

    interface OnItemClickListener {
        void onItemClick(LocalMedia localMedia);
    }
}
