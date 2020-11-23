package ooo.poorld.mycard.model.card.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.entity.CardImage;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class CertInfoAdapter extends RecyclerView.Adapter<CertInfoAdapter.ViewHolder> {
    private Context mContext;

    private List<CardImage> mCardImages;

    public CertInfoAdapter(Context context) {
        this.mContext = context;
        mCardImages = new ArrayList<>();
    }

    public void addImage(CardImage certEntity) {
        mCardImages.add(certEntity);
        notifyDataSetChanged();
    }

    public void addImages(List<CardImage> certEntities) {
        mCardImages.clear();
        mCardImages.addAll(certEntities);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_cert_info, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CardImage entity = mCardImages.get(i);
        Bitmap bitmap = BitmapFactory.decodeFile(entity.getFilePath());
        viewHolder.info_iv.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return mCardImages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView info_iv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            info_iv = itemView.findViewById(R.id.info_iv);
        }
    }
}
