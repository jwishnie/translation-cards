package org.mercycorps.translationcards.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.inject.Inject;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.media.CardAudioClickListener;
import org.mercycorps.translationcards.media.MediaPlayerManager;

import java.util.Arrays;
import java.util.List;


public class TranslationCardListAdapter extends ArrayAdapter<Dictionary.Translation> {

    private final DbManager dbm;
    private static final int REQUEST_KEY_EDIT_CARD = 2;
    private final MediaPlayerManager mediaPlayerManager;
    private Deck deck;
    private Context context;
    private Dictionary[] dictionaryList;
    private boolean[] translationCardStates;
    private int dictionaryIndex;

    public TranslationCardListAdapter(Context context, int resource, int textViewResourceId,
                                      List<Dictionary.Translation> objects, MediaPlayerManager mediaPlayerManager, Deck deck, DbManager dbm) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.mediaPlayerManager = mediaPlayerManager;
        this.deck = deck;
        this.dbm = dbm;
    }

    public void setDictionary(Dictionary[] dictionaryList, int dictionaryIndex) {
        this.dictionaryList = dictionaryList;
        this.dictionaryIndex = dictionaryIndex;
        translationCardStates = new boolean[dictionaryList[dictionaryIndex].getTranslationCount()];
        Arrays.fill(translationCardStates, false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.translation_item, parent, false);
        }

        if (translationCardStates[position]) {
            convertView.findViewById(R.id.translation_child).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.indicator_icon).setBackgroundResource(
                    R.drawable.collapse_arrow);
        } else {
            convertView.findViewById(R.id.translation_child).setVisibility(View.GONE);
            convertView.findViewById(R.id.indicator_icon).setBackgroundResource(R.drawable.expand_arrow);
        }

        Dictionary.Translation translation = getItem(position);

        ProgressBar progressBar = (ProgressBar) convertView.findViewById(
                R.id.list_item_progress_bar);
        CardAudioClickListener cardAudioClickListener = new CardAudioClickListener(translation.getFilename(),
                progressBar, mediaPlayerManager);

        TextView cardTextView = (TextView) convertView.findViewById(R.id.origin_translation_text);
        cardTextView.setText(translation.getLabel());
        cardTextView.setOnClickListener(cardAudioClickListener);

        TextView translatedText = (TextView) convertView.findViewById(R.id.translated_text);
        setTranslatedText(translatedText, translation);
        convertView.findViewById(R.id.translated_text_layout).setOnClickListener(cardAudioClickListener);

        convertView.findViewById(R.id.translation_indicator_layout)
                .setOnClickListener(new CardIndicatorClickListener(convertView, position));

        convertView.setOnClickListener(null);

        View editView = convertView.findViewById(R.id.translation_card_edit);
        View deleteView = convertView.findViewById(R.id.translation_card_delete);

        if (deck.isLocked()) {
            editView.setVisibility(View.GONE);
            deleteView.setVisibility(View.GONE);
        } else {
            editView.setOnClickListener(new CardEditClickListener(getItem(position)));
            deleteView.setOnClickListener(new CardDeleteClickListener(getItem(position).getDbId()));
        }

        return convertView;
    }

    private void setTranslatedText(TextView translatedText, Dictionary.Translation translation) {
        if(translation.getTranslatedText().isEmpty()){
            populateEmptyTranslatedText(translatedText);
        } else {
            populateTranslatedText(translatedText, translation);
        }
    }

    private void populateTranslatedText(TextView translatedText, Dictionary.Translation translation) {
        translatedText.setText(translation.getTranslatedText());
        translatedText.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryTextColor));
        translatedText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
    }

    private void populateEmptyTranslatedText(TextView translatedText) {
        translatedText.setText(String.format(context.getString(R.string.translated_text_hint),
                dictionaryList[dictionaryIndex].getLabel()));
        translatedText.setTextColor(ContextCompat.getColor(getContext(), R.color.textDisabled));
        translatedText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
    }

    public class CardIndicatorClickListener implements View.OnClickListener {

        private View translationItem;
        private int position;

        public CardIndicatorClickListener(View translationItem, int position) {

            this.translationItem = translationItem;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            View translationChild = translationItem.findViewById(R.id.translation_child);
            if (translationChild.getVisibility() == View.GONE) {
                translationChild.setVisibility(View.VISIBLE);
                translationItem.findViewById(R.id.indicator_icon).setBackgroundResource(
                        R.drawable.collapse_arrow);
                translationCardStates[position] = true;
            } else {
                translationChild.setVisibility(View.GONE);
                translationItem.findViewById(R.id.indicator_icon).setBackgroundResource(
                        R.drawable.expand_arrow);
                translationCardStates[position] = false;
            }
        }
    }

    public class CardEditClickListener implements View.OnClickListener {
        private Dictionary.Translation translationCard;

        public CardEditClickListener(Dictionary.Translation translationCard) {
            this.translationCard = translationCard;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, RecordingActivity.class);
            intent.putExtra(RecordingActivity.INTENT_KEY_DICTIONARY_ID, dictionaryList[dictionaryIndex].getDbId());
            intent.putExtra(RecordingActivity.INTENT_KEY_DICTIONARY_LABEL, dictionaryList[dictionaryIndex].getLabel());
            intent.putExtra(RecordingActivity.INTENT_KEY_TRANSLATION_ID, translationCard.getDbId());
            intent.putExtra(RecordingActivity.INTENT_KEY_TRANSLATION_LABEL,
                    translationCard.getLabel());
            intent.putExtra(RecordingActivity.INTENT_KEY_TRANSLATION_IS_ASSET,
                    translationCard.getIsAsset());
            intent.putExtra(RecordingActivity.INTENT_KEY_TRANSLATION_FILENAME,
                    translationCard.getFilename());
            intent.putExtra(RecordingActivity.INTENT_KEY_TRANSLATION_TEXT,
                    translationCard.getTranslatedText());
            intent.putExtra(DecksActivity.INTENT_KEY_DECK_ID, deck);

            ((TranslationsActivity)context).startActivityForResult(intent, REQUEST_KEY_EDIT_CARD);
        }
    }

    private class CardDeleteClickListener implements View.OnClickListener {
        long translationId;

        public CardDeleteClickListener(long translationId) {
            this.translationId = translationId;
        }

        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Flashcard")
                    .setMessage("Are you sure you want to delete this translation card?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dbm.deleteTranslation(translationId);
                            dictionaryList = dbm.getAllDictionariesForDeck(deck.getDbId());
                            setDictionary(dictionaryList, dictionaryIndex);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }
}
