package tinn.meal.ping.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import tinn.meal.ping.R;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.Method;

public class ChildActivity extends AppCompatActivity {
    protected int layoutResID;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(layoutResID);
            toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }
        } catch (Exception e) {
            Method.log(e);
            Method.hit(this, e.getMessage());
        }
    }

    protected String getValue(int id) {
        View view = findViewById(id);
        if (view instanceof EditText) {
            return ((EditText) view).getText().toString();
        }
        if (view instanceof Spinner) {
            return ((LoadInfo) ((Spinner) view).getSelectedItem()).Message;
        }
        return null;
    }

    protected void setValue(int id, Object value) {
        View view = findViewById(id);
        if (view instanceof EditText) {
            ((EditText) view).setText(value.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Method.log("onBackPressed");
            onBackPressed();
        }
        return true;
    }
}
