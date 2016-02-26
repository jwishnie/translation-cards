package org.mercycorps.translationcards.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationCardListAdapterTest {

    public static final String ORIGIN_TEXT = "Label";
    public static final String TRANSLATED_TEXT = "TranslatedText";
    private TranslationCardListAdapter translationCardListAdapter;

    @Before
    public void setUp() throws Exception {
        translationCardListAdapter = new TranslationCardListAdapter(RuntimeEnvironment.application,
                R.layout.translation_item, R.id.origin_translation_text, new ArrayList<Dictionary.Translation>());

        translationCardListAdapter
                .add(new Dictionary.Translation(ORIGIN_TEXT, false, "Filename", 0, TRANSLATED_TEXT));
    }

    @Test
    public void getView_shouldDisplayOriginText() {
        View listItem = translationCardListAdapter.getView(0, null, null);

        TextView originText = (TextView) listItem.findViewById(R.id.origin_translation_text);

        assertThat(originText.getText().toString(), is(ORIGIN_TEXT));
    }

    @Test
    public void getView_shouldDisplayDestinationText() {
        View listItem = translationCardListAdapter.getView(0, null, null);
        listItem.findViewById(R.id.translation_child).setVisibility(View.VISIBLE);

        TextView translatedText = (TextView) listItem.findViewById(R.id.translated_text);

        assertThat(translatedText.getText().toString(), is(TRANSLATED_TEXT));
    }

    @Test
    public void getView_shouldShowExpandIndicatorWhenCardIsCollapsed() {
        View listItem = translationCardListAdapter.getView(0, null, null);

        ImageView cardIndicator = (ImageView) listItem.findViewById(R.id.indicator_icon);

        assertThat(shadowOf(cardIndicator.getBackground()).getCreatedFromResId(), is(R.drawable.expand_arrow));
    }

    @Ignore
    @Test
    public void getView_shouldShowCollapseIndicatorWhenCardIsExpanded() {
        View listItem = translationCardListAdapter.getView(0, null, null);
        listItem.findViewById(R.id.translation_child).setVisibility(View.VISIBLE);

        ImageView cardIndicator = (ImageView) listItem.findViewById(R.id.indicator_icon);

        assertThat(shadowOf(cardIndicator.getBackground()).getCreatedFromResId(), is(R.drawable.collapse_arrow));
    }

    @Test
    public void onClick_shouldOpenCardWhenIClickOnCardIndicator() {
        View listItem = translationCardListAdapter.getView(0, null, null);

        ImageView cardIndicator = (ImageView) listItem.findViewById(R.id.indicator_icon);

        assertThat(shadowOf(cardIndicator).getOnClickListener(), is(CardIndicatorClickListener.class));
    }
}