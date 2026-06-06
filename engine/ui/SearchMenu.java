package dev.levia.jimi.engine.ui;

import java.lang.foreign.Arena;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import dev.levia.jimi.engine.input.Input;
import dev.levia.jimi.engine.render.Render;
import dev.levia.jimi.util.trans.GLFW;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class SearchMenu extends Menu<SearchMenu> {
    private List<String> items = new ArrayList<>();
    public boolean[] filteredItems;
    public int longestWordInItems = 0;
    public String filterText = "";
    private int limit = 8;
    private int centerW = 0, centerH = 0;
    private static final int margin = 25;
    private int cursorPosition = 0;
    private int selectedIndex = 0;

    private int selectionIndex = -1;
    private boolean startFromSelection = false;
    private int trues = -1;
    private Arena alloc = Arena.ofShared();
    public Consumer<String> callback;

    public SearchMenu(Render render, List<String> items) {
        // x y is based on center of the window
        // width and height is calculated based on longestWordInItems

        var longestWordInItems = items.stream().max(Comparator.comparing(String::length)).orElse("something").length();
        // generate normal rectangle in the middle of screen
        // generate input field
        // generate items to select
        var font = render.fonts.getFirst();
        var width = (longestWordInItems * font.charWidth())/2+margin;
        var height = font.height()* min(20, items.size());
        System.out.printf("PopupMenu {x: %d, y: %d, longest: %d, fontWidth: %d}\n", width, height, longestWordInItems, font.charWidth());
        super(render, render.width/2-width, render.height/2-height, 2*width, height*2);
        this.longestWordInItems = longestWordInItems;
        filteredItems = new boolean[items.size()];
        Arrays.fill(filteredItems, true);
        this.items.addAll(items);
        this.render = render;
        this.font = font;
        this.isFocus = true;
        updateSearch();
        Input.clickCallbacks.add((button, action, mods) -> {
            // check if click is inside of popup and if is not then close self and request removal
        });
    }
    public final Input.KeyCallback call = (key, scancode, action, mods) -> {
        if (!isFocus) return;
        if (action != Input.PRESS && action != Input.REPEAT) return;
        var next = true;

        if (((key == Input.BACKSPACE && cursorPosition >= 0) || (key == Input.DELETE && cursorPosition <= filterText.length())) && !filterText.isEmpty() ) {
            if (selectionIndex != -1) {
                filterText = filterText.substring(0, startFromSelection ? cursorPosition:selectionIndex)+filterText.substring(startFromSelection ?selectionIndex:cursorPosition);
                cursorPosition = startFromSelection ? cursorPosition: selectionIndex;
                selectionIndex = -1;
            } else {
                if (key == Input.BACKSPACE) {
                    cursorPosition--;
                    filterText = filterText.substring(0, cursorPosition) + filterText.substring(cursorPosition+1);
                } else {
                    filterText = filterText.substring(0, cursorPosition) + filterText.substring(cursorPosition-1);
                }
            }
        } else if ((mods & Input.CONTROL_BIT) != 0) {
            if (key == Input.A) {
                cursorPosition = filterText.length();
                selectionIndex = 0;
                startFromSelection = false;
                // select all text so the user can delete it or copy it or overwrite it
                next = false;
            } else if (key == Input.C) {
                if (selectionIndex == -1) GLFW.setClipboardString(render.window, alloc.allocateFrom(filterText));
                else GLFW.setClipboardString(render.window, alloc.allocateFrom(!startFromSelection ? filterText.substring(selectedIndex, cursorPosition) : filterText.substring(cursorPosition, selectionIndex)));
                next = false;
            } else if (key == Input.X) {
                if (selectionIndex == -1) {
                    GLFW.setClipboardString(render.window, alloc.allocateFrom(filterText));
                    cursorPosition = 0;
                    selectionIndex = 0;
                    filterText = "";
                } else {
                    GLFW.setClipboardString(render.window, alloc.allocateFrom(!startFromSelection ? filterText.substring(selectedIndex, cursorPosition) : filterText.substring(cursorPosition, selectionIndex)));
                    filterText = !startFromSelection ? filterText.substring(0, selectedIndex) + filterText.substring(cursorPosition) : filterText.substring(0, cursorPosition) + filterText.substring(selectionIndex);
                    cursorPosition = startFromSelection ? cursorPosition : selectionIndex;
                    selectionIndex = -1;
                }
                next = false;
            } else if (key == Input.V) {
                var clipboard = GLFW.getClipboardString(render.window).reinterpret(Long.MAX_VALUE);
                var clip = clipboard.getString(0);
                System.out.println(clip);
                if (selectionIndex == -1) {
                    filterText = filterText.substring(0, cursorPosition) + clip + filterText.substring(cursorPosition);
                    cursorPosition += clip.length();
                } else {
                    // replace selected text by the clipboard
                    var start = filterText.substring(0, startFromSelection ? cursorPosition : selectionIndex);
                    filterText = start + clip + filterText.substring(startFromSelection ? selectionIndex : cursorPosition);
                    cursorPosition = start.length() + clip.length();
                    selectionIndex = -1;
                }
                // paste the text
                next = false;
            }
        }
        if (!next) return;
        else if ((key >= Input.A && key <= Input.Z)) {
            // should check where is cursor if its on the end and character length is too long we should display only last characters of search
            // simply put user should see only based on cursor position but not override the outside of the box (longestWordInItems*font.charWidth())
            if ((mods & Input.SHIFT_BIT) == 0) filterText += (char) (key + 32); else filterText += (char) key;
            selectionIndex = -1;
            cursorPosition++;
            System.out.println(filterText+"|"+cursorPosition+"|SHIFT: "+(mods & Input.SHIFT_BIT));
        } else if ((key >= Input._0 && key <= Input._9) || (key >= Input.KP_0 && key <= Input.KP_9 )  || key == Input.SPACE || key == Input.PERIOD || key == Input.COMMA) {
            filterText += (char) key;
            selectionIndex = -1;
            cursorPosition++;
        } else if (key == Input.LEFT && cursorPosition > 0) {
            cursorPosition--;
            if ((mods & Input.SHIFT_BIT) == 1) {
                if (selectionIndex == -1) selectionIndex = cursorPosition+1;
            } else selectionIndex = -1;
            System.out.printf("<|cursor|> {%d, %d, shift: %d}\n", selectionIndex, cursorPosition, (mods & Input.SHIFT_BIT));
            startFromSelection = cursorPosition < selectionIndex;
        }
        else if (key == Input.RIGHT && cursorPosition < filterText.length()) {
            cursorPosition++;

            if ((mods & Input.SHIFT_BIT) == 1) {
                if (selectionIndex == -1) selectionIndex = cursorPosition-1;
            } else selectionIndex = -1;

            System.out.printf("<|cursor|> {%d, %d}\n", selectionIndex, cursorPosition);
            startFromSelection = cursorPosition < selectionIndex;
        }
        else if (key == Input.DOWN) {
            selectedIndex = min(trues-1, selectedIndex+1);
        } else if (key == Input.UP) {
            selectedIndex = max(0, selectedIndex-1);
        } else if ((key == Input.ENTER || key == Input.KP_ENTER) && selectedIndex != -1) {
            var counter = selectedIndex;
            for (int i = 0; i < items.size(); i++)
                if (filteredItems[i] && counter-- == 0) {
                    isFocus = false;
                    callback.accept(items.get(i));
                }
        }
        trues = 0;
        for (int i = 0; i < items.size(); i++) {
            filteredItems[i] = items.get(i).toLowerCase().contains(filterText.toLowerCase());
            trues += filteredItems[i] ? 1 : 0;
        }
    };
    public void updateSearch() {
        centerW = render.width/2-longestWordInItems*font.width()-margin;
        centerH = render.height/2-font.height()-margin;
        cursorPosition = 0;
        selectionIndex = -1;
        selectedIndex = 0;
        filterText = "";
        isFocus = true;
        trues = 0;
        for (int i = 0; i < items.size(); i++) {
            filteredItems[i] = items.get(i).toLowerCase().contains(filterText.toLowerCase());
            trues += filteredItems[i] ? 1 : 0;
        }
        Input.keyCallbacks.add(call);
    }

    @Override
    public void draw() {
        drawRect(0, 0, render.width, render.height, new SmallColor(0.2f, 0.2f, 0.2f, 0.5f), false);
        super.draw();

        // Render Cursor
        super.drawRect(cursorPosition*font.charWidth()+x+margin-2, y+margin+1, 2, font.height()-6, new SmallColor(1f, 1f, 1f, 1f), true);
        // draw blue rect from one point to another using cursorPosition * charWidth and selectionIndex * charWidth (use startFromSelection to determine which one is start)
        if (selectionIndex != -1) {
            if (!startFromSelection) super.drawRect(x+margin-2+(selectionIndex*font.charWidth()), y+margin+1, ((cursorPosition-selectionIndex)* font.charWidth()), font.height()-6, new SmallColor(0.2f, 0.2f, 0.2f, 1f), false);
            else super.drawRect(x+margin-2+(cursorPosition*font.charWidth()), y+margin+1, ((selectionIndex-cursorPosition)* font.charWidth()), font.height()-6, new SmallColor(0.2f, 0.2f, 0.2f, 1f), false);
        }

        if (selectedIndex != -1 && trues != 0) super.drawRect(x+margin-5, (y+margin+50)-5+(selectedIndex)*font.height(), width-margin*2+10, font.height(), new SmallColor(0.2f, 0.2f, 0.2f, 1f), false);
        super.text(filterText.isEmpty() ? "Searching..." : filterText, x+margin, y+margin);
        for (int i = 0, counter = 0; i < filteredItems.length; i++) {
            if (filteredItems[i]) text(items.get(i), x+margin, 50+y + (counter++)*font.height()+margin);
        }
    }
}
