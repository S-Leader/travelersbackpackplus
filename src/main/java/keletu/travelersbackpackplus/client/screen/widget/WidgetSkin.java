package keletu.travelersbackpackplus.client.screen.widget;

import keletu.travelersbackpackplus.menu.layout.WidgetLayout;

public class WidgetSkin {
    public static final int DEFAULT_LAYER = 75;

    public int layer() {
        return DEFAULT_LAYER;
    }

    public void paint(BackgroundComposer c, WidgetLayout layout, int widget) {
    }

    public void paintOver(BackgroundComposer c, WidgetLayout layout, int widget) {
    }

    public String emptyTip() {
        return null;
    }
}
