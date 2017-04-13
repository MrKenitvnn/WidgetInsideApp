
> Copy anim/

> Create layout/view_widget layout/view_widget_trash

> Drawable: ic_myg_touch.png bubble_trash_background.png

> Code: com/mozaa/widget/

#### How to use?

```java

// create 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        widgetManager = new WidgetManager.Builder(MainActivity.this)
                .setWidgetLayout(R.layout.view_widget)
                .setTrashLayout(R.layout.view_widget_trash)
                .build();
        widgetManager.initialize();
        
        widgetManager.setOnWidgetClickListener(this);
        widgetManager.setOnWidgetRemoveListener(this);
    }


// do show
@OnClick(R.id.button_show)
void onButtonShowWidgetTap () {
    widgetManager.showWidget();
}   

// do hide
@OnClick(R.id.button_hide)
void onButtonHideWidgetTap () {
    widgetManager.hideWidget();
}



// implements OnWidgetClickListener, OnWidgetRemoveListener

    @Override
    public void onWidgetRemoveListener() {
        Toast.makeText(this, "Bubble REMOVE", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWidgetClickListener(View view) {
        Toast.makeText(this, "Bubble clicked : show panel", Toast.LENGTH_SHORT).show();
    }


```
