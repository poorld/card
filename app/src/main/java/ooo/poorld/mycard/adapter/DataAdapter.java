package ooo.poorld.mycard.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import ooo.poorld.mycard.model.data.DataManageActivity;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.entity.FileData;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private Context mContext;
    private List<FileData> mFileData;

    public DataAdapter(Context context, List<FileData> fileData) {
        this.mContext = context;
        this.mFileData = fileData;
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
                setDrawable(viewHolder.file_image_type, R.mipmap.file_icon_xls);
                break;
            default:
                setDrawable(viewHolder.file_image_type, R.drawable.file);
                break;
        }

        if (fileData.isDirectory()) {
            setDrawable(viewHolder.file_image, R.drawable.directory);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // DataManageActivity.startActivity(mContext, fileData.getFilePath());
                }
            });
        }

    }

    private void setImage(boolean image1Show, boolean image2Show, Drawable drawable) {

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
