package org.mercycorps.translationcards.activity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.media.CardAudioClickListener;

import java.util.List;

public class TranslationCardListAdapter extends ArrayAdapter<Dictionary.Translation> {

    public TranslationCardListAdapter(
            Context context, int resource, int textViewResourceId,
            List<Dictionary.Translation> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.translation_item, parent, false);
        }

//        if (translationCardStates[position]) {
//            convertView.findViewById(R.id.translation_child).setVisibility(View.VISIBLE);
//            convertView.findViewById(R.id.indicator_icon).setBackgroundResource(
//                    R.drawable.collapse_arrow);
//        } else {
//            convertView.findViewById(R.id.translation_child).setVisibility(View.GONE);
//            convertView.findViewById(R.id.indicator_icon).setBackgroundResource(
//                    R.drawable.expand_arrow);
//        }
//
//        convertView.setOnClickListener(null);
//
//        convertView.findViewById(R.id.translation_indicator_layout)
//                .setOnClickListener(new CardIndicatorClickListener(convertView, position));
//
//        View editView = convertView.findViewById(R.id.translation_card_edit);
//        View deleteView = convertView.findViewById(R.id.translation_card_delete);
//        if (deck.isLocked()) {
//            editView.setVisibility(View.GONE);
//            deleteView.setVisibility(View.GONE);
//        } else {
//            editView.setOnClickListener(new CardEditClickListener(getItem(position)));
//            deleteView.setOnClickListener(new CardDeleteClickListener(getItem(position).getDbId()));
//        }
//
        TextView cardTextView = (TextView) convertView.findViewById(
                R.id.origin_translation_text);
        cardTextView.setText(getItem(position).getLabel());
//
//        ProgressBar progressBar = (ProgressBar) convertView.findViewById(
//                R.id.list_item_progress_bar);
//        cardTextView.setOnClickListener(new CardAudioClickListener(getItem(position).getFilename(),
//                progressBar, lastMediaPlayerManager));
//
//        TextView translatedText = (TextView) convertView.findViewById(R.id.translated_text);
//        if(getItem(position).getTranslatedText().isEmpty()){
//            translatedText.setText(String.format(getString(R.string.translated_text_hint),
//                    dictionaries[currentDictionaryIndex].getLabel()));
//            translatedText.setTextColor(ContextCompat.getColor(getContext(),
//                    R.color.textDisabled));
//            translatedText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
//        } else {
//            translatedText.setText(getItem(position).getTranslatedText());
//            translatedText.setTextColor(ContextCompat.getColor(getContext(),
//                    R.color.primaryTextColor));
//            translatedText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
//        }
//
//        convertView.findViewById(R.id.translated_text_layout)
//                .setOnClickListener(new CardAudioClickListener(getItem(position).getFilename(), progressBar,
//                        lastMediaPlayerManager));

        return convertView;
    }

    @Override
    public Dictionary.Translation getItem(int position) {
        return super.getItem(position - 1);
    }
}
