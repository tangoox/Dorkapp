// Generated code from Butter Knife. Do not modify!
package com.septinary.xbwapp.fragment.main.stu;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class StuMenuFirstFragment$$ViewInjector<T extends com.septinary.xbwapp.fragment.main.stu.StuMenuFirstFragment> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230792, "field 'fmsf_classlist'");
    target.fmsf_classlist = finder.castView(view, 2131230792, "field 'fmsf_classlist'");
    view = finder.findRequiredView(source, 2131230790, "field 'fmsf_refreshview'");
    target.fmsf_refreshview = finder.castView(view, 2131230790, "field 'fmsf_refreshview'");
    view = finder.findRequiredView(source, 2131230791, "field 'fmsf_rollad'");
    target.fmsf_rollad = finder.castView(view, 2131230791, "field 'fmsf_rollad'");
  }

  @Override public void reset(T target) {
    target.fmsf_classlist = null;
    target.fmsf_refreshview = null;
    target.fmsf_rollad = null;
  }
}
