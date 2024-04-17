package com.msaggik.thirdlessonpalettefordrawing10;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.SensorEvent;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    // поля
    private ImageView buttonMenu;
    private LinearLayout buttons;
    private boolean buttonsCheck = false; // поле включения кнопок
    private ImageView buttonPalette, buttonClear;
    private ArtView art;



    //поля менеджера сенсоров и акселерометра
    private SensorManager sensorManager;
    private Sensor accelerometer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // привязка кнопок к разметке
        buttonMenu = findViewById(R.id.buttonMenu);
        buttons = findViewById(R.id.buttons);
        buttonPalette = findViewById(R.id.buttonPalette);
        buttonClear = findViewById(R.id.buttonClear);
        art = findViewById(R.id.art);



        //Получаем менеджер устройств и акселерометр
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



        // обработка нажатия кнопок
        buttonMenu.setOnClickListener(listener);
        buttonPalette.setOnClickListener(listener);
        buttonClear.setOnClickListener(listener);
    }


    //слушатель (акселерометра), считывает новые данные датчиков
    private SensorEventListener sensorEventListener = new SensorEventListener(){
        @Override
        @SuppressLint("SetTextI18n")
        //при изменении данных датчика идет метод
        public void onSensorChanged(SensorEvent sensorEvent){
            //получение датчика сгенерировавшего событие
            Sensor multiSensor = sensorEvent.sensor;

            //если изменения произошли на акселерометре
            if (multiSensor.getType() == Sensor.TYPE_ACCELEROMETER){
                //значения(положения) полученные из ивента акселерометра
                float xAccelerometer = sensorEvent.values[0]; //ускорение по оси X (поперечное направление)
                float yAccelerometer = sensorEvent.values[1]; //ускорение по оси Y (продольное направление)
                float zAccelerometer = sensorEvent.values[2]; //ускорение по оси Z (вертикальное направление)

                //среднее значение ускорения по всем осям
                float medianAccelerometer = (xAccelerometer + yAccelerometer + zAccelerometer) / 3;
                //если телефон в условиях ускорения, то
                if (medianAccelerometer > 10){
                    //при встряхивании кнопка становится видимой или невидимой
                    if (buttonsCheck){
                        buttonsCheck = false;
                        buttons.setVisibility(View.INVISIBLE);
                    } else{
                        buttonsCheck = true;
                        buttons.setVisibility(View.VISIBLE);
                    }

                }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume(){
        super.onResume();
        //регистрация сенсоров (задание слушателя)
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause(){
        super.onPause();
        //отзыв регистрации сенсоров (отключение слушателя)
        sensorManager.unregisterListener(sensorEventListener);
    }







    // слушатель для кнопок
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.buttonMenu:
                    if (buttonsCheck) {
                        buttonsCheck = false;
                        buttons.setVisibility(View.INVISIBLE);
                    } else {
                        buttonsCheck = true;
                        buttons.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.buttonClear:
                    // код для очистки View
                    AlertDialog.Builder broomDialog = new AlertDialog.Builder(MainActivity.this); // создание диалогового окна типа AlertDialog
                    broomDialog.setTitle("Очистка рисунка"); // заголовок диалогового окна
                    broomDialog.setMessage("Очистить область рисования (имеющийся рисунок будет удалён)?"); // сообщение диалога

                    broomDialog.setPositiveButton("Да", new DialogInterface.OnClickListener(){ // пункт выбора "да"
                        public void onClick(DialogInterface dialog, int which){
                            art.clear(); // метод очистки кастомизированного View
                            dialog.dismiss(); // закрыть диалог
                        }
                    });
                    broomDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener(){  // пункт выбора "нет"
                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel(); // выход из диалога
                        }
                    });
                    broomDialog.show(); // отображение на экране данного диалога
                    break;
            }
        }
    };

}