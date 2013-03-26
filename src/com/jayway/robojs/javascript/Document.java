package com.jayway.robojs.javascript;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

import android.widget.TextView;

public final class Document extends ScriptableObject {

    private static final long serialVersionUID = 9128611814755244219L;

    private TextView output;

    public Document() {
        this.output = null;
    }

    @Override
    public String getClassName() {
        return "document";
    }

    public void setOutputContainer(TextView container) {
        this.output = container;
    }

    @JSFunction
    public void write(String content) {
        if (output != null) {
            output.append(content + "\n");
        }
    }
}
