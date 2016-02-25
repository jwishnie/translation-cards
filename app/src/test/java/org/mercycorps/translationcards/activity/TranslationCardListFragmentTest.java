package org.mercycorps.translationcards.activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.hepler.TestActivity;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationCardListFragmentTest {

    @Test
    public void onCreate_shouldNotBeNull() {
        TestActivity translationsActivity = Robolectric.buildActivity(TestActivity.class).create().get();
        TranslationCardListFragment translationCardListFragment = new TranslationCardListFragment();

        translationsActivity.getSupportFragmentManager().beginTransaction().add(translationCardListFragment, "test").commit();

        assertThat(translationsActivity.getSupportFragmentManager().findFragmentByTag("test"), is(notNullValue()));
    }
}
