package com.jayway.robojs.javascript;

import java.lang.reflect.InvocationTargetException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public final class JavaScriptRunner {
    private static final String SCHEME = "http://";

    private final Context context;
    private final Scriptable scope;
    private String result;

    public JavaScriptRunner() {
        context = Context.enter();
        context.setOptimizationLevel(-1);
        scope = context.initStandardObjects();
    }

    public void defineVariable(String name, String body) {
        ScriptableObject.putProperty(scope, name, body);
    }

    public void executeJavaScript(final android.content.Context appContext, final String source) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... uri) {
                String script = null;

                // Fetch the business logic script from Internet
                if (uri != null && uri.length == 1) {
                    String s = uri[0];
                    script = s.startsWith(SCHEME) ? JavaScriptDownloader.downloadScript(s) : s;
                    Log.d("MYTAG", "SCRIPT: " + script);
                }

                return script;
            }

            @Override
            protected void onPostExecute(String result) {
                // Try to execute the script
                try {
                    if (result == null || result.length() <= 0) {
                        throw new IllegalArgumentException("Script mustn't be empty");
                    }

                    Object r = context.evaluateString(scope, result, "rhino", 1, null);
                    result = r != null ? Context.toString(r) : null;
                } catch (Throwable t) {
                    Log.e("MYTAG", "Couldn't execute the JavaScript: " + result, t);
                    Toast.makeText(appContext, "Couldn't execute the JavaScript", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(source);
    }

    public void exit() {
        Context.exit();
    }

    public Object initObject(String name, Class<? extends ScriptableObject> clasz) {
        Object instance = null;

        try {
            if (name != null && clasz != null) {
                ScriptableObject.defineClass(scope, clasz);
                instance = context.newObject(scope, name, null);
                ScriptableObject.putProperty(scope, name, instance);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return instance;
    }

    public String getLastExecutionResult() {
        return result;
    }
}
