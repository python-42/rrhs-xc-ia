package rrhs.xc.ia.util;

import java.util.List;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

/**
 * This class implements ChangeListener<String>. The purpose of this class is to
 * filter a given list of objects based on their name. The name is determined
 * based on the given objects implementation of toString().
 * The <code>ObservableList</code> should be the list that is somehow being
 * displayed. The content of this list will be changed based on the input
 * string. The reference <code>List</code> should contain all of the possible
 * values. Its elements will be filtered and placed in to the
 * <code>ObservableList</code>. The items in the <code>List</code> will not be
 * changed.
 */
public class SearchChangeListener<T> implements ChangeListener<String> {

    private final ObservableList<T> viewableList;
    private final List<T> referenceList;

    public SearchChangeListener(ObservableList<T> viewableList, List<T> referenceList) {
        this.viewableList = viewableList;
        this.referenceList = referenceList;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldStr, String newStr) {
        viewableList.clear();
        if (newStr.isBlank()) {
            viewableList.addAll(referenceList);
        } else {
            for (T i : referenceList) {
                if (newStr.trim().equals(i.toString()) || StringUtils.matchesRegex(Pattern.quote(newStr)+ "[0-9]*[a-z]*", Pattern.quote(i.toString()))) {
                    viewableList.add(i);
                }
            }
        }
    }

}
