package com.jayway.robojs;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jayway.robojs.javascript.Document;
import com.jayway.robojs.javascript.JavaScriptRunner;

public class MainActivity extends Activity {

    private TextView result;
    private JavaScriptRunner js;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize layout
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);

        // Trigger the JavaScript when the 'calculate' button is pressed.
        final EditText input = (EditText) findViewById(R.id.input);
        final Button calculate = (Button) findViewById(R.id.button);
        calculate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View target) {
                String celsius = input.getEditableText().toString();
                String script = "app.setCelsius(" + celsius + "); app.showResult(' F');";
                js.executeJavaScript(MainActivity.this, script);
            }
        });
    }

    @Override
    protected void onDestroy() {
        // Make sure the JavaScript engine is released properly
        js.exit();
        js = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        switch (menuId) {
        case R.id.menu_reset:
            // Clear all previous output
            result.setText(null);
            return false;
        case R.id.menu_reload:
            // Reload the online JavaScript
            initJavaScript();
            return false;
        case R.id.menu_settings:
            // Yet to be implemented
            return false;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void initJavaScript() {
        if (js != null) {
            js.exit();
            js = null;
        }

        // Initialize the JavaScript engine
        js = new JavaScriptRunner();

        Document document = (Document) js.initObject("document", Document.class);
        document.setOutputContainer(result);

        js.executeJavaScript(MainActivity.this, "http://js-android.urszuly.com/app.js");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initJavaScript();
    }
}
