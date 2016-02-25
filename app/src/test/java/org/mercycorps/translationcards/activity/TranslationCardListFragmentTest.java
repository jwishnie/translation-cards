package org.mercycorps.translationcards.activity;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.hepler.TestActivity;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationCardListFragmentTest {

    public static final String TRANSLATION_LABEL = "TranslationLabel";
    public static final String NO_VALUE = "";
    public static final long DEFAULT_LONG = -1;
    public static final String TRANSLATED_TEXT = "TranslatedText";
    public static final String DICTIONARY_TEST_LABEL = "TestLabel";
    public static final int DEFAULT_DECK_ID = 1;

    private TestActivity testActivity;
    private TranslationCardListFragment fragment;

    @Before
    public void setUp() throws Exception {
        fragment = new TranslationCardListFragment();
        startFragment(fragment);
    }

    @Test
    public void shouldNotBeNull() {
        assertThat(fragment, is(notNullValue()));
    }

    @Test
    public void onCreateView_shouldHaveTranslationCardListView() {
        assertThat(fragment.getListView(), is(notNullValue()));
    }

    @Test
    public void onActivityCreated_shouldHaveTranslationCardListAdapter() {
        assertThat(fragment.getListAdapter(), is(TranslationCardListAdapter.class));
    }

    @Test
    public void shouldHaveOneItemInAdapter() {
        fragment.setDictionary(newTestDictionary());

        assertThat(fragment.getListView().getAdapter().getCount(), is(1));
    }

    @Test
    public void getView_shouldHaveOriginText() {
        fragment.setDictionary(newTestDictionary());
        ListView translationsList = fragment.getListView();
        View translationsListItem = translationsList.getAdapter().getView(1, null, translationsList);

        TextView originTranslationText = (TextView) translationsListItem.findViewById(
                R.id.origin_translation_text);
        assertThat(originTranslationText.getText().toString(), is(TRANSLATION_LABEL));
    }

    private Dictionary newTestDictionary() {
        Dictionary.Translation[] translations = { new Dictionary.Translation(TRANSLATION_LABEL, false,
                NO_VALUE, DEFAULT_LONG, TRANSLATED_TEXT) };
        return new Dictionary(DICTIONARY_TEST_LABEL, translations, DEFAULT_LONG,
                DEFAULT_DECK_ID);
    }

}
