package lu.dainesch.fakehttp.view.filter;

import com.airhacks.afterburner.views.FXMLView;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import lu.dainesch.fakehttp.server.filters.FilterType;
import lu.dainesch.fakehttp.view.filter.custom.CustomView;
import lu.dainesch.fakehttp.view.filter.fileserve.FileServeView;
import lu.dainesch.fakehttp.view.filter.fixed.FixedStatusView;
import lu.dainesch.fakehttp.view.filter.forward.ForwardView;


public class FilterFactory {

    private static final ResourceBundle FILTER_BUNDLE = ResourceBundle.getBundle("Filters");

    private static final Map<FilterType, FXMLView> VIEWS = new HashMap<>();

    private FilterFactory() {
    }

    public static FXMLView getFilterView(FilterType type) {
        switch (type) {
            case FixedStatus:
                VIEWS.putIfAbsent(type, new FixedStatusView());
                break;
            case FileDownload:
                VIEWS.putIfAbsent(type, new FileServeView());
                break;
            case Forward:
                VIEWS.putIfAbsent(type, new ForwardView());
                break;
            case Custom: 
                VIEWS.putIfAbsent(type, new CustomView());
                break;
            default:
                throw new IllegalArgumentException("Unknow filter type: " + type);
        }
        return VIEWS.get(type);
    }

    public static FilterHandler<?> getFilterHandler(FilterType type) {
        return (FilterHandler<?>) VIEWS.get(type).getPresenter();
    }

    public static String getName(FilterType type) {
        return FILTER_BUNDLE.getString(type.toString() + ".name");
    }

    public static String getDescription(FilterType type) {
        return FILTER_BUNDLE.getString(type.toString() + ".desc");
    }

}
