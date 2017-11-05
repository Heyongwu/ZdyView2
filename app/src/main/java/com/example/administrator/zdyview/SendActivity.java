package com.example.administrator.zdyview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity implements View.OnClickListener {


    private final static int SCANNIN_GREQUEST_CODE = 1;
    /**
     * 扫描二维码
     */
    private Button mBt1;
    /**
     * 生成二维码
     */
    private Button mBt2;
    /**
     * 按钮3
     */
    private Button mBt3;
    private TextView mTv;
    private ImageView mIv;
    private LinearLayout mActivitySend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        initView();

        new DisplayImageOptions.Builder()
                .showImageForEmptyUri(null)
                .showImageOnFail(null)
                .showImageOnFail(null).build();
    }

    private void initView() {
        mBt1 = (Button) findViewById(R.id.bt1);
        mBt1.setOnClickListener(this);
        mBt2 = (Button) findViewById(R.id.bt2);
        mBt2.setOnClickListener(this);
        mBt3 = (Button) findViewById(R.id.bt3);
        mBt3.setOnClickListener(this);
        mTv = (TextView) findViewById(R.id.tv);
        mIv = (ImageView) findViewById(R.id.iv);
        mActivitySend = (LinearLayout) findViewById(R.id.activity_send);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.bt1:
                //扫描二维码
                Intent intent = new Intent(SendActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
                break;
            case R.id.bt2:
                Bitmap bitmap = generateBitmap("http://www.sina.com", 200, 200);
                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                Bitmap addLogo = addLogo(bitmap, logo);
                mIv.setImageBitmap(addLogo);
                break;
            case R.id.bt3:
                break;
        }
    }

    /**
     * 添加图标
     *
     * @param qrBitmap
     * @param logoBitmap
     * @return
     */
    private Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
        int qrBitmapWidth = qrBitmap.getWidth();
        int qrBitmapHeight = qrBitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        float scaleSize = 1.0f;
        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
            scaleSize *= 2;
        }
        float sx = 1.0f / scaleSize;
        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
        canvas.restore();
        return blankBitmap;
    }

    //生成二维码
    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                Log.i("xxx", result.toString());
                Intent intent = new Intent(SendActivity.this, WebViewActivity.class);
                intent.putExtra("url", result);
                startActivity(intent);
            }
        }
    }
}
