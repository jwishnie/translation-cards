package org.mercycorps.translationcards.activity;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;

import java.util.ArrayList;

public class TranslationCardListFragment extends ListFragment {

    private Dictionary dictionary;
    private TranslationCardListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translation_card_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listAdapter = new TranslationCardListAdapter(getActivity(), R.layout.translation_item,
                R.id.origin_translation_text, new ArrayList<Dictionary.Translation>());
        setListAdapter(listAdapter);
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;

        listAdapter.clear();
        for (Dictionary.Translation translation : dictionary.getTranslations()) {
            listAdapter.add(translation);
        }
    }
}
