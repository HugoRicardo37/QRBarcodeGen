package com.devxh.qrcodegenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ImageView qrview,imageback,barcodeview,resetnumbers,camera;
    private Button btqrcode,btbarcode,btprintbarcode;
    private EditText numerocartao;
    private TextView textviewnumerocartao,barcodenumber;
    private Bitmap barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            Objects.requireNonNull(getSupportActionBar()).hide();

            numerocartao = findViewById(R.id.numerocartao);
            resetnumbers = findViewById(R.id.deletenumbers);
            qrview = findViewById(R.id.qrview);
            btqrcode = findViewById(R.id.qrcodebutton);
            btbarcode= findViewById(R.id.barcodebutton);
            imageback = findViewById(R.id.imageback);
            barcodeview = findViewById(R.id.barcodeview);
            barcodenumber = findViewById(R.id.textViewbarcodenumber);
            textviewnumerocartao = findViewById(R.id.textviewnumerocartao);
            btprintbarcode = findViewById(R.id.btPrint);
            camera = findViewById(R.id.imgCam);

            imageback.setVisibility(View.INVISIBLE);
            btprintbarcode.setVisibility(View.INVISIBLE);
            imageback.setOnClickListener(view -> {

                qrview.setVisibility(View.INVISIBLE);
                barcodeview.setVisibility(View.INVISIBLE);
                barcodenumber.setVisibility(View.INVISIBLE);
                textviewnumerocartao.setVisibility(View.VISIBLE);
                numerocartao.setVisibility(View.VISIBLE);
                btbarcode.setVisibility(View.VISIBLE);
                btqrcode.setVisibility(View.VISIBLE);
                imageback.setVisibility(View.INVISIBLE);
                resetnumbers.setVisibility(View.VISIBLE);
                camera.setVisibility(View.VISIBLE);
                btprintbarcode.setVisibility(View.INVISIBLE);



            });
            camera.setVisibility(View.VISIBLE);
            camera.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this,QrScannActivity.class);
                startActivity(intent);
            });




            qrview.setVisibility(View.INVISIBLE);
            btqrcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(numerocartao.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this, "Favor Inserir os números", Toast.LENGTH_SHORT).show();
                    }else {

                        String number = numerocartao.getText().toString();

                        Bitmap qrCode = generateQRCode(number, 500, 500);
                        qrview.setImageBitmap(qrCode);
                        qrview.setVisibility(View.VISIBLE);
                        imageback.setVisibility(View.VISIBLE);
                        barcodeview.setVisibility(View.INVISIBLE);
                        barcodenumber.setVisibility(View.INVISIBLE);

                       // getInvisible();
                    }

                }
                private Bitmap generateQRCode(String content, int width, int height) {
                    Map<EncodeHintType, Object> hints = new HashMap<>();
                    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                    try {
                        QRCodeWriter writer = new QRCodeWriter();
                        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
                        int bitMatrixWidth = bitMatrix.getWidth();
                        int bitMatrixHeight = bitMatrix.getHeight();
                        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

                        for (int y = 0; y < bitMatrixHeight; y++) {
                            int offset = y * bitMatrixWidth;
                            for (int x = 0; x < bitMatrixWidth; x++) {
                                pixels[offset + x] = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                            }
                        }
                        Bitmap bitmap ;
                        bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_8888);
                        bitmap.setPixels(pixels, 0, bitMatrixWidth, 0, 0, bitMatrixWidth, bitMatrixHeight);
                        return bitmap;
                    } catch (WriterException e) {
                        e.printStackTrace();
                        return null;
                    }

                }
            });

            barcodeview.setVisibility(View.INVISIBLE);
            barcodenumber.setVisibility(View.INVISIBLE);
            btbarcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(numerocartao.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this, "Favor Inserir os números", Toast.LENGTH_SHORT).show();
                    }else {

                        String number = numerocartao.getText().toString();
                        barcodenumber.setText(number);
                        try {
                            barcode = generateBarcode(number);
                            barcodeview.setImageBitmap(barcode);
                            barcodeview.setVisibility(View.VISIBLE);
                            barcodenumber.setVisibility(View.VISIBLE);
                            qrview.setVisibility(View.INVISIBLE);
                            imageback.setVisibility(View.VISIBLE);
                            btprintbarcode.setVisibility(View.VISIBLE);

                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }

                }

                public Bitmap generateBarcode(String barcodeData) throws WriterException {

                    Code128Writer writer = new Code128Writer();
                    BitMatrix bitMatrix = writer.encode(barcodeData, BarcodeFormat.CODE_128, 180,100);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {

                            bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                        }
                    }


                    return bitmap;
                }

            });

            resetnumbers.setOnClickListener(view -> resetNumbers());

            btprintbarcode.setOnClickListener(view -> {

                printBarcodeAsPNG(barcode);
                // printQrCodeasPDF();
            });



        }

        private void printBarcodeAsPNG(Bitmap barcodeBitmap) {
            // Crie uma instância da PrintHelper
            PrintHelper printHelper = new PrintHelper(this);
            // Configura a escala da imagem para ajustá-la à página
            printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            // Imprime a imagem
            printHelper.printBitmap("Nome do Documento", barcodeBitmap);
        }




        private void resetNumbers(){
            numerocartao.setText("");

        }





}