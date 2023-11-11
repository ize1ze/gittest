package com.example.bmicalculator;

import static android.provider.BaseColumns._ID;

import static com.example.bmicalculator.Constants.BMI;
import static com.example.bmicalculator.Constants.DATE;
import static com.example.bmicalculator.Constants.TABLE_NAME;
import static com.example.bmicalculator.Constants.TITLE;
import static com.example.bmicalculator.Constants.WIGHT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    DBHelper dbh;
    private EditText heightEditText;
    private EditText weightEditText;
    private Button calculateButton;
    private ImageView historyButton;
    private TextView resultTextView;
    private TextView resultTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText edit_text = (EditText)findViewById(R.id.weight);
        EditText edit_text2 = (EditText)findViewById(R.id.height);
        edit_text.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        edit_text2.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        heightEditText = findViewById(R.id.height);
        weightEditText = findViewById(R.id.weight);
        calculateButton = findViewById(R.id.btn);
        resultTextView = findViewById(R.id.result);
        resultTextView2 = findViewById(R.id.result2);
        historyButton = findViewById(R.id.history);


        dbh = new DBHelper(this);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float h = Float.parseFloat(heightEditText.getText().toString()) / 100;
                float w = Float.parseFloat(weightEditText.getText().toString());
                float res = w / (h * h);
                DecimalFormat formatter = new DecimalFormat("#,###.##");
                String formattedRes = formatter.format(res);
                resultTextView.setText(formattedRes);

                if (res < 16) {
                    resultTextView2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red2));
                    resultTextView2.setText(getString(R.string.severethinness));
                } else if (res >= 16 && res <= 17) {
                    resultTextView2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.pink));
                    resultTextView2.setText(getString(R.string.moderatethinness));
                } else if (res > 17 && res <= 18.5) {
                    resultTextView2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.yellow2));
                    resultTextView2.setText(getString(R.string.mildthinness));
                } else if (res >= 18.5 && res <= 25) {
                    resultTextView2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
                    resultTextView2.setText(getString(R.string.normal));
                } else if (res > 25 && res <= 30) {
                    resultTextView2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.yellow2));
                    resultTextView2.setText(getString(R.string.overweight));
                } else if (res > 30 && res <= 35) {
                    resultTextView2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.pink));
                    resultTextView2.setText(getString(R.string.obese1));
                } else if (res > 35 && res <= 40) {
                    resultTextView2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red2));
                    resultTextView2.setText(getString(R.string.obese2));
                } else {
                    resultTextView2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red));
                    resultTextView2.setText(getString(R.string.obese3));
                }
                addEvent();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Cursor cursor = getEvents();
                    showEvents(cursor);
                }finally{
                    dbh.close();
                }
            }
        });
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final TextView txtView1=findViewById(R.id.result);
        final TextView txtView2=findViewById(R.id.result2);
        final TextView txtView7=findViewById(R.id.result3);
        final TextView txtView3=findViewById(R.id.bmi);
        final TextView txtView4=findViewById(R.id.yourweight);
        final TextView txtView5=findViewById(R.id.yourheight);
        final TextView txtView6=findViewById(R.id.btn);
        txtView1.setTextSize(newConfig.fontScale*26);
        txtView2.setTextSize(newConfig.fontScale*20);
        txtView3.setTextSize(newConfig.fontScale*26);
        txtView4.setTextSize(newConfig.fontScale*20);
        txtView5.setTextSize(newConfig.fontScale*20);
        txtView6.setTextSize(newConfig.fontScale*20);
        txtView7.setTextSize(newConfig.fontScale*26);

        final TextView txtView = findViewById(R.id.result);
        String originalText = txtView.getText().toString();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            txtView.setText(originalText);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            txtView.setText(originalText);
        }
    }

    private void addEvent() {
        TextView et1 = (TextView) weightEditText;
        TextView et2 = resultTextView;
        TextView et3 = (TextView) resultTextView2;
        String string1 = String.format("%1$s", et1.getText());
        String string2 = String.format("%1$s", et2.getText());
        String string3 = String.format("%1$s", et3.getText());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date());
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, formattedDate);
        values.put(WIGHT, string1);
        values.put(BMI, string2);
        values.put(TITLE, string3);
        db.insert(TABLE_NAME, null, values);
    }//end addEvent
    private Cursor getEvents() {
        String[] FROM = {_ID, DATE, WIGHT, BMI, TITLE};
        String ORDER_BY = _ID + " DESC";
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null, null, ORDER_BY);
        return cursor;
    }//end getEvents
    private void showEvents(Cursor cursor) {
        setContentView(R.layout.history);
        final ListView listView = (ListView)findViewById(R.id.listView);
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        while(cursor.moveToNext()) {
            map = new HashMap<String, String>();
            map.put("id", String.valueOf(cursor.getLong(0)));
            map.put("date", cursor.getString(1));
            map.put("wight", cursor.getString(2));
            map.put("bmi", cursor.getString(3));
            map.put("title", cursor.getString(4));
            MyArrList.add(map);
        }
        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter( MainActivity.this, MyArrList, R.layout.activity_colum,
                new String[] { "date", "wight", "bmi", "title"},
                new int[] { R.id.col_date,R.id.col_weight,R.id.col_bmi, R.id.col_title} );
        listView.setAdapter(sAdap);
    }//end showEvents

}
class DecimalDigitsInputFilter implements InputFilter {
    private Pattern mPattern;
    DecimalDigitsInputFilter(int digits, int digitsAfterZero) {
        mPattern = Pattern.compile("[0-9]{0," + (digits - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) +
                "})?)||(\\.)?");
    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = mPattern.matcher(dest);
        if (!matcher.matches())
            return "";
        return null;
    }
}

