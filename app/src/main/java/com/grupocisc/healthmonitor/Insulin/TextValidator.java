package com.grupocisc.healthmonitor.Insulin;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by Gema on 28/02/2017.
 */


public abstract class TextValidator implements TextWatcher {
    private static final String TAG = "[TextValidator]";
    private final EditText editText;

    public TextValidator(EditText editText) {
        this.editText = editText;
    }

    public abstract void validate(EditText editText, String text);

    @Override
    final public void afterTextChanged(Editable s) {
        String Method ="[afterTextChanged]";
        Log.i(TAG, Method + "Init..."  );
        String text = editText.getText().toString();
        validate(editText, text);
        Log.i(TAG, Method + "End..."  );
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */
        String Method ="[beforeTextChanged]";
        Log.i(TAG, Method + "Init..."  );
        Log.i(TAG, Method + "End..."  );
    }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */
        String Method ="[onTextChanged]";
        Log.i(TAG, Method + "Init..."  );
        Log.i(TAG, Method + "End..."  );
    }
}