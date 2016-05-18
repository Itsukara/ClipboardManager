package itsukara.clipboardmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

class CustomListItemAdapter extends ArrayAdapter<String> {
    private List<String> clipList;
    private ClipboardManager clipboard;
    private EditText editTextClipboard;
    private LayoutInflater layoutInflater;

    public CustomListItemAdapter(Context context, List<String> clipList, ClipboardManager clipboard, EditText editTextClipboard) {
        super(context, 0, clipList);
        this.clipList = clipList;
        this.clipboard = clipboard;
        this.editTextClipboard = editTextClipboard;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int index = position;
        View view;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.custom_list_item, parent, false);
        } else {
            view = convertView;
        }

        String item = getItem(position);
        final EditText editText = (EditText)view.findViewById(R.id.editText);
        editText.setText(item);

        Button buttonPaste = (Button)view.findViewById(R.id.buttonPaste);
        buttonPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = clipboard.getPrimaryClip();
                String pasteString = "";
                if (clip != null) {
                    ClipData.Item item = clip.getItemAt(0);
                    pasteString = item.getText().toString();
                }

                editText.setText(pasteString);
                clipList.set(index, pasteString);
                Log.d("Info", "clipList.set("+index+", "+pasteString+")");
            }
        });

        Button buttonCopy = (Button)view.findViewById(R.id.buttonCopy);
        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String copyString = editText.getText().toString();
                clipList.set(index, copyString);
                Log.d("Info", "clipList.set("+index+", "+copyString+")");

                ClipData clip = ClipData.newPlainText("copied_text", copyString);
                clipboard.setPrimaryClip(clip);
                editTextClipboard.setText(copyString);
            }
        });

        /* 下記、予想外の動きになるため封印
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newString = s.toString();
                Log.d("Info", "TextChanged: index ="+index+", newString ="+newString);
            }
        });
        */

        return view;
    }
}
