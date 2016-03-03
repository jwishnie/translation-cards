package org.mercycorps.translationcards.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.AbstractModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.media.CardAudioClickListener;
import org.mercycorps.translationcards.media.MediaPlayerManager;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;
import java.util.List;

import roboguice.RoboGuice;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationCardListAdapterTest {

    public static final String ORIGIN_TEXT = "Label";
    public static final String TRANSLATED_TEXT = "TranslatedText";
    public static final String DICTIONARY_LABEL = "DictionaryLabel";
    private TranslationCardListAdapter translationCardListAdapter;
    private DbManager dbManager;
    private Dictionary.Translation translation;

    @Before
    public void setUp() throws Exception {
        initializeMockDbManager();
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application,
                new TranslationCardListAdapterTestModule());
        RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(this);

        Intent intent = new Intent();
        intent.putExtra("Deck", mock(Deck.class));
        TranslationsActivity translationsActivity = Robolectric.buildActivity(TranslationsActivity.class).withIntent(intent).create().get();
        ArrayList<Dictionary.Translation> translations = new ArrayList<>();
        translationCardListAdapter = new TranslationCardListAdapter(translationsActivity,
                R.layout.translation_item, R.id.origin_translation_text, new ArrayList<Dictionary.Translation>(),
                mock(MediaPlayerManager.class), mock(Deck.class));

        Dictionary dictionary = mock(Dictionary.class);
        when(dictionary.getLabel()).thenReturn(DICTIONARY_LABEL);
        when(dictionary.getTranslationCount()).thenReturn(2);
        translationCardListAdapter.setDictionary(new Dictionary[]{dictionary}, 0);

        translation = new Dictionary.Translation(ORIGIN_TEXT, false, "Filename", 0, TRANSLATED_TEXT);
        translationCardListAdapter.add(translation);

        translationCardListAdapter
                .add(new Dictionary.Translation(ORIGIN_TEXT, false, "Filename", 0, null));
    }

    @Test
    public void getView_shouldDisplayOriginText() {
        View listItem = translationCardListAdapter.getView(0, null, null);

        TextView originText = (TextView) listItem.findViewById(R.id.origin_translation_text);

        assertThat(originText.getText().toString(), is(ORIGIN_TEXT));
    }

    @Test
    public void getView_shouldAttachCardAudioClickListenerToOriginText() {
        View listItem = translationCardListAdapter.getView(0, null, null);

        TextView originText = (TextView) listItem.findViewById(R.id.origin_translation_text);

        assertThat(shadowOf(originText).getOnClickListener(), is(CardAudioClickListener.class));
    }

    @Test
    public void getView_shouldDisplayDestinationText() {
        View listItem = translationCardListAdapter.getView(0, null, null);
        listItem.findViewById(R.id.translation_child).setVisibility(View.VISIBLE);

        TextView translatedText = (TextView) listItem.findViewById(R.id.translated_text);

        assertThat(translatedText.getText().toString(), is(TRANSLATED_TEXT));
    }

    @Test
    public void getView_shouldDisplayHintTextWhenNoValueForDestinationText() {
        View listItem = translationCardListAdapter.getView(1, null, null);
        listItem.findViewById(R.id.translation_child).setVisibility(View.VISIBLE);

        TextView translatedText = (TextView) listItem.findViewById(R.id.translated_text);

        assertThat(translatedText.getText().toString(), is("Add " + DICTIONARY_LABEL + " translation"));
    }

    @Test
    public void getView_shouldAttachCardAudioClickListenerToTranslatedText() {
        View listItem = translationCardListAdapter.getView(0, null, null);
        listItem.findViewById(R.id.translation_child).setVisibility(View.VISIBLE);

        View translatedTextLayout = listItem.findViewById(R.id.translated_text_layout);

        assertThat(shadowOf(translatedTextLayout).getOnClickListener(), is(CardAudioClickListener.class));
    }

    @Test
    public void getView_shouldShowExpandIndicatorWhenCardIsCollapsed() {
        View listItem = translationCardListAdapter.getView(0, null, null);

        ImageView cardIndicator = (ImageView) listItem.findViewById(R.id.indicator_icon);

        assertThat(shadowOf(cardIndicator.getBackground()).getCreatedFromResId(), is(R.drawable.expand_arrow));
    }

    @Test
    public void getView_shouldShowChildViewWhenCardIndicatorIsClicked() {
        View listItem = translationCardListAdapter.getView(0, null, null);

        View cardIndicator = listItem.findViewById(R.id.translation_indicator_layout);
        cardIndicator.performClick();

        assertThat(listItem.findViewById(R.id.translation_child).getVisibility(), is(View.VISIBLE));
    }

    @Test
    public void getView_shouldShowCollapseIndicatorWhenCardIsExpanded() {
        View listItem = translationCardListAdapter.getView(0, null, null);
        listItem.findViewById(R.id.translation_indicator_layout).performClick();

        ImageView cardIndicator = (ImageView) listItem.findViewById(R.id.indicator_icon);

        assertThat(shadowOf(cardIndicator.getBackground()).getCreatedFromResId(), is(R.drawable.collapse_arrow));
    }

    @Test
    public void getView_shouldSetNoClickListenerToListItem() {
        View listItem = translationCardListAdapter.getView(0, null, null);

        assertThat(shadowOf(listItem).getOnClickListener(), is(nullValue()));
    }

    @Test
    public void getView_shouldStartRecordingActivityWhenEditButtonIsClicked() {
        View listItem = translationCardListAdapter.getView(0, null, null);

        View editCardLayout = listItem.findViewById(R.id.translation_card_edit);

        editCardLayout.performClick();

        Intent nextStartedActivity = shadowOf((TranslationsActivity)translationCardListAdapter.getContext()).getNextStartedActivity();
        assertThat(nextStartedActivity.getComponent().getClassName(), is(RecordingActivity.class.getCanonicalName()));
    }

    @Test
    public void getView_shouldDisplayAlertDialogWhenDeleteButtonIsClicked() {
        View listItem = translationCardListAdapter.getView(0, null, null);
        View deleteCardLayout = listItem.findViewById(R.id.translation_card_delete);

        deleteCardLayout.performClick();

        assertNotNull(shadowOf(ShadowAlertDialog.getLatestAlertDialog()));
    }

    @Test
    public void getView_shouldRemoveCardWhenDeleteButtonIsClickedInDialog() {
        View listItem = translationCardListAdapter.getView(0, null, null);

        listItem.findViewById(R.id.translation_card_delete).performClick();
        ShadowAlertDialog.getLatestAlertDialog().getButton(AlertDialog.BUTTON_POSITIVE).performClick();

        verify(dbManager).deleteTranslation(translation.getDbId());
    }

    private void initializeMockDbManager() {
        dbManager = mock(DbManager.class);
        Dictionary dictionary = new Dictionary("", new Dictionary.Translation[0], 0, 0);
        when(dbManager.getAllDictionariesForDeck(anyInt())).thenReturn(new Dictionary[] {dictionary});
    }

    private class TranslationCardListAdapterTestModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(DbManager.class).toInstance(dbManager);
        }
    }
}