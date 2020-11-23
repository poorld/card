package ooo.poorld.mycard.model.cert.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.entity.CardImage;
import ooo.poorld.mycard.entity.Certificate;
import ooo.poorld.mycard.model.cert.CertInfoAct;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class CertAdapter extends RecyclerView.Adapter<CertAdapter.ViewHolder> {
    private Context mContext;

    private List<Certificate> mCertEntities;

    public CertAdapter(Context context) {
        this.mContext = context;
        mCertEntities = new ArrayList<>();
    }

    public void addCert(Certificate certEntity) {
        mCertEntities.add(certEntity);
        notifyDataSetChanged();
    }

    public void addCerts(List<Certificate> certEntities) {
        mCertEntities.clear();
        mCertEntities.addAll(certEntities);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.activity_certificate_item, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Certificate entity = mCertEntities.get(i);
        viewHolder.cert_title.setText(entity.getCertificateName());
        viewHolder.cert_note.setText(entity.getNote());
        List<CardImage> images = entity.getCardImages();

        if (images != null && images.size() > 0) {
            Bitmap bitmap = BitmapFactory.decodeFile(images.get(0).getFilePath());
            viewHolder.profile_image.setImageBitmap(bitmap);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CertInfoAct.startActivity(mContext, entity.getCertificateID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCertEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cert_title;
        TextView cert_note;
        CircleImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cert_title = itemView.findViewById(R.id.cert_title);
            cert_note = itemView.findViewById(R.id.cert_note);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}
