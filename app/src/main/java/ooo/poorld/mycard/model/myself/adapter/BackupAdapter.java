package ooo.poorld.mycard.model.myself.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.entity.Upload;
import ooo.poorld.mycard.view.PopupBackup;

/**
 * author: teenyda
 * date: 2020/12/12
 * description:
 */
public class BackupAdapter extends RecyclerView.Adapter<BackupAdapter.ViewHolder>{

    private Context mContext;
    private List<Upload> mUploads;
    private PopupBackup.ItemClickListener mItemClickListener;

    public BackupAdapter(Context context) {
        mContext = context;
        mUploads = new ArrayList<>();
    }

    public void addList(List<Upload> uploads) {
        mUploads.clear();
        mUploads.addAll(uploads);
        notifyDataSetChanged();
    }

    public void setItemClickListener(PopupBackup.ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_backup, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Upload upload = mUploads.get(position);
        holder.backup_date.setText(upload.getUploadDate());
        holder.backup_size.setText(String.valueOf(upload.getFileSize() / 1024 / 1024) + "MB");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(upload);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView backup_date;
        TextView backup_size;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            backup_date = itemView.findViewById(R.id.backup_date);
            backup_size = itemView.findViewById(R.id.backup_size);
        }
    }

}
