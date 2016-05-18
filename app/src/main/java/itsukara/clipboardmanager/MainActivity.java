package itsukara.clipboardmanager;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> clipList;
    private EditText editTextClipboard;
    private ClipboardManager clipboard;
    private SharedPreferences pref;
    private final int clipboardSize = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = this.getBaseContext();

        editTextClipboard = (EditText)findViewById(R.id.editTextClipboard);
        clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

        clipList = new ArrayList<>();
        for (int i = 0; i < clipboardSize; i++) {
            clipList.add("");
        }
        ListView listView = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new CustomListItemAdapter(context, clipList, clipboard, editTextClipboard);
        assert listView != null;
        listView.setAdapter(adapter);
    }

    @SuppressLint({"DefaultLocale", "CommitPrefEdits"})
    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = pref.edit();
        for (int i = 0; i < clipboardSize; i++) {
            String clipString = clipList.get(i);
            editor.putString("clip"+String.format("%02d", i), clipString);
            Log.d("Info", "clip"+String.format("%02d", i)+":"+clipString);
        }
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        pref = getSharedPreferences("clipboard_strings", Context.MODE_PRIVATE);
        for (int i = 0; i < clipboardSize; i++) {
            @SuppressLint("DefaultLocale") String clipString = pref.getString("clip" + String.format("%02d", i), "");
            clipList.set(i, clipString);
        }

        ClipData clip = clipboard.getPrimaryClip();
        String pasteString = "";
        if (clip != null) {
            ClipData.Item item = clip.getItemAt(0);
            pasteString = item.getText().toString();
        }
        editTextClipboard.setText(pasteString);
    }

}
