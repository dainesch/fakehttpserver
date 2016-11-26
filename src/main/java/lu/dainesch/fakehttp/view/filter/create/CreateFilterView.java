package lu.dainesch.fakehttp.view.filter.create;

import com.airhacks.afterburner.views.FXMLView;


public class CreateFilterView extends FXMLView {

    private boolean initialized = false;

    @Override
    public CreateFilterPresenter getPresenter() {
        if (!initialized) {
            getView();
            initialized = true;
        }
        return (CreateFilterPresenter) super.getPresenter();
    }

    public void show() {
        if (!initialized) {
            getView();
            initialized = true;
        }
        getPresenter().showDialog();
    }

}
