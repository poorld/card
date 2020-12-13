package ooo.poorld.mycard.model.data.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.zhuliang.appchooser.AppChooser;
import ooo.poorld.mycard.BuildConfig;
import ooo.poorld.mycard.MyselfActivity;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.common.CommonPopView;
import ooo.poorld.mycard.entity.FileData;
import ooo.poorld.mycard.utils.Tools;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private Context mContext;
    private List<FileData> mFileData;
    private CommonPopView mCommonPopView;
    private FileDeletedListener mFileDeletedListener;

    public DataAdapter(Context context) {
        this.mContext = context;
        this.mFileData = new ArrayList<>();
        this.mCommonPopView = new CommonPopView(context);
        mCommonPopView.setTitle("提示");
        mCommonPopView.setMessage("您确定要删除该文件吗？");
        mCommonPopView.setLeftTitle("取消");
        mCommonPopView.setRightTitle("确定");
    }

    public interface FileDeletedListener{
        void onFileDeleted(FileData fileData);
    }

    public void setFileDeletedListener(FileDeletedListener fileDeletedListener) {
        this.mFileDeletedListener = fileDeletedListener;
    }

    public void addFile(List<FileData> fileData) {
        mFileData.clear();
        mFileData.addAll(fileData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.activity_data_item, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FileData fileData = mFileData.get(i);
        viewHolder.file_name.setText(fileData.getFileName());

        String fileType = fileData.getFileType().toLowerCase();
        viewHolder.file_image.setVisibility(View.GONE);
        viewHolder.file_image_type.setVisibility(View.VISIBLE);
        switch (fileType) {
            case "jpg":
            case "jpeg":
            case "png":
                Bitmap bitmap = getThumb(fileData.getFilePath());
                viewHolder.file_image.setVisibility(View.VISIBLE);
                viewHolder.file_image_type.setVisibility(View.GONE);
                viewHolder.file_image.setImageBitmap(bitmap);
                // setDrawable(viewHolder.file_image, R.drawable.image);
                break;
            case "mp3":
                setDrawable(viewHolder.file_image_type, R.mipmap.file_icon_audio);
                break;
            case "mp4":
                setDrawable(viewHolder.file_image_type, R.mipmap.file_icon_video);
                break;
            case "docx":
                setDrawable(viewHolder.file_image_type, R.mipmap.file_icon_doc);
                break;
            case "doc":
                setDrawable(viewHolder.file_image_type, R.mipmap.file_icon_doc);
                break;
            case "pdf":
                setDrawable(viewHolder.file_image_type, R.mipmap.file_icon_pdf);
                break;
            case "xls":
            case "xlsx":
                setDrawable(viewHolder.file_image_type, R.mipmap.file_icon_xls);
                break;
            default:
                setDrawable(viewHolder.file_image_type, R.drawable.file);
                break;
        }

        /*if (fileData.isDirectory()) {
            setDrawable(viewHolder.file_image, R.drawable.directory);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // DataManageActivity.startActivity(mContext, fileData.getFilePath());
                }
            });
        }*/

        // 选择打开方式
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppChooser.from((FragmentActivity) mContext)
                        .file(new File(fileData.getFilePath()))
                        .requestCode(20001)
                        .authority(BuildConfig.APPLICATION_ID + ".fileprovider")
                        .load();
            }
        });
        // 长按删除
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mCommonPopView.show(viewHolder.file_name, fileData);
                return true;
            }
        });

        mCommonPopView.setOnBtnClick(new CommonPopView.OnBtnClick() {
            @Override
            public void onLeftClick(FileData fileData) {
                mCommonPopView.dismiss();
            }

            @Override
            public void onRightClick(FileData fileData) {
                String filePath = fileData.getFilePath();
                File file = new File(filePath);
                if (file.delete()) {
                    Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                }
                if (mFileDeletedListener != null) {
                    mFileDeletedListener.onFileDeleted(fileData);
                }
                mCommonPopView.dismiss();
            }
        });

    }

    private void startDoc(ViewHolder viewHolder, File file) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.browseDocuments((Activity) mContext, file,20001);
            }
        });
    }


    /**
     * 生成缩略图
     * 缩略图是将原图等比压缩，压缩后宽、高中较小的一个等于198像素
     */
    private Bitmap getThumb(String path){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int reqWidth, reqHeight, width=options.outWidth, height=options.outHeight;
        if (width > height){
            reqWidth = 198;
            reqHeight = (reqWidth * height)/width;
        }else{
            reqHeight = 198;
            reqWidth = (width * reqHeight)/height;
        }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        try{
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            Matrix mat = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            ExifInterface ei =  new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    mat.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    mat.postRotate(180);
                    break;
            }
            //            Log.e("test","bitmap.getWidth():"+bitmap.getWidth()+",,bitmap.getHeight():"+bitmap.getHeight());
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private void setDrawable(ImageView iv, int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv.setImageDrawable(mContext.getDrawable(res));
        }else {
            iv.setImageDrawable(mContext.getResources().getDrawable(res));
        }
    }


    @Override
    public int getItemCount() {
        return mFileData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView file_image;
        ImageView file_image_type;
        TextView file_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            file_image = itemView.findViewById(R.id.file_image);
            file_image_type = itemView.findViewById(R.id.file_image_type);
            file_name = itemView.findViewById(R.id.file_name);
        }
    }
}
