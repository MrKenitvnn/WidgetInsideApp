package mozaa.demobubbleinsideapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.mozaa.widget.OnWidgetClickListener;
import com.mozaa.widget.OnWidgetRemoveListener;
import com.mozaa.widget.WidgetManager;
import com.mozaa.demobubbleinsideapp.R;

public class MainActivity extends AppCompatActivity implements OnWidgetClickListener, OnWidgetRemoveListener{

    WidgetManager widgetManager;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        widgetManager = new WidgetManager.Builder(MainActivity.this)
                .setWidgetLayout(R.layout.view_widget)
                .setTrashLayout(R.layout.view_widget_trash)
                .build();
        widgetManager.initialize();
        widgetManager.setOnWidgetClickListener(this);
        widgetManager.setOnWidgetRemoveListener(this);

        Button buttonShow = (Button) findViewById(R.id.button_show);
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                widgetManager.showWidget();
            }
        });


        Button buttonHide = (Button) findViewById(R.id.button_hide);
        buttonHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                widgetManager.hideWidget();
            }
        });
    }


    @Override
    public void onWidgetRemoveListener() {
        Toast.makeText(this, "Bubble REMOVE", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWidgetClickListener(View view) {
        Toast.makeText(this, "Bubble clicked : show panel", Toast.LENGTH_SHORT).show();
    }
}
