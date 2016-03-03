package org.mercycorps.translationcards.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.AbstractModule;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;

import roboguice.RoboGuice;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Test for TranslationsActivity
 *
 * @author patdale216@gmail.com (Pat Dale)
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationsActivityTest {

    public static final int DEFAULT_DECK_ID = 1;
    public static final String NO_VALUE = "";
    public static final long DEFAULT_LONG = -1;
    public static final String DICTIONARY_TEST_LABEL = "TestLabel";
    public static final String TRANSLATED_TEXT = "TranslatedText";
    public static final String TRANSLATION_LABEL = "TranslationLabel";
    public static final String DEFAULT_DECK_NAME = "Default";
    private TranslationsActivity translationsActivity;
    private DbManager dbManagerMock;
    private Dictionary.Translation translation;
    private boolean NOT_LOCKED;

    @Before
    public void setUp() {
        RoboGuice.setUseAnnotationDatabases(false);
        Intent intent = new Intent();
        NOT_LOCKED = false;
        Deck deck = new Deck(DEFAULT_DECK_NAME, NO_VALUE, NO_VALUE, DEFAULT_DECK_ID, DEFAULT_LONG, NOT_LOCKED);
        intent.putExtra("Deck", deck);
        initializeMockDbManager();
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application,
                new TranslationsActivityTestModule());
        RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(this);
        translationsActivity = Robolectric.buildActivity(TranslationsActivity.class).withIntent(
                intent).create().get();
    }

    private void initializeMockDbManager() {
        dbManagerMock = mock(DbManager.class);
        Dictionary[] dictionaries = new Dictionary[1];
        translation = new Dictionary.Translation(TRANSLATION_LABEL, false, NO_VALUE, DEFAULT_LONG,
                TRANSLATED_TEXT);
        Dictionary.Translation emptyTranslatedTextTranslation = new Dictionary.Translation(
                TRANSLATION_LABEL, false, NO_VALUE, DEFAULT_LONG,
                NO_VALUE);
        Dictionary.Translation[] translations = {translation, emptyTranslatedTextTranslation};
        dictionaries[0] = new Dictionary(DICTIONARY_TEST_LABEL, translations, DEFAULT_LONG,
                DEFAULT_DECK_ID);
        when(dbManagerMock.getAllDictionariesForDeck(DEFAULT_DECK_ID)).thenReturn(dictionaries);
    }

    @Test
    public void onCreate_shouldShowDeckNameInToolbar() {
        assertThat(translationsActivity.getSupportActionBar().getTitle().toString(), is(
                DEFAULT_DECK_NAME));
    }

    @Test
    public void onCreate_dbmGetsCalled() {
        verify(dbManagerMock).getAllDictionariesForDeck(DEFAULT_DECK_ID);
    }

    @Test
    public void initTabs_shouldShowLanguageTabWhenOnHomeScreen() {
        LinearLayout tabContainer = (LinearLayout) translationsActivity.findViewById(R.id.tabs);

        assertThat(tabContainer.getChildCount(), is(1));

        View languageTab = tabContainer.getChildAt(0);
        TextView languageTabText = (TextView) languageTab.findViewById(R.id.tab_label_text);
        assertThat(languageTabText.getText().toString(), is(DICTIONARY_TEST_LABEL.toUpperCase()));
    }

    @Test
    public void setDictionary_shouldNotHaveAnyTranslationCardsWhenNoneHaveBeenCreated() {
        TextView translationCardText = (TextView) translationsActivity.findViewById(
                R.id.origin_translation_text);

        assertThat(translationCardText, is(nullValue()));
    }

    @Test
    public void shouldGoToDecksActivityWhenBackButtonPressed() {
        ShadowActivity shadowActivity = Shadows.shadowOf(translationsActivity);

        shadowActivity.onBackPressed();
        assertTrue(shadowActivity.isFinishing());
    }

    public class TranslationsActivityTestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(DbManager.class).toInstance(dbManagerMock);
        }

    }

    @Ignore
    @Test
    public void onPause_shouldStopPlayingMediaPlayerManagerWhenActivityPaused() {
    }
}