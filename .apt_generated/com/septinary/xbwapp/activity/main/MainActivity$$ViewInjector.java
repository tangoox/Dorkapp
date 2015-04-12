// Generated code from Butter Knife. Do not modify!
package com.septinary.xbwapp.activity.main;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class MainActivity$$ViewInjector<T extends com.septinary.xbwapp.activity.main.MainActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230846, "field 'ml_menu'");
    target.ml_menu = finder.castView(view, 2131230846, "field 'ml_menu'");
    view = finder.findRequiredView(source, 2131230815, "field 'main_reside'");
    target.main_reside = finder.castView(view, 2131230815, "field 'main_reside'");
    view = finder.findRequiredView(source, 2131230840, "field 'mr_classlist'");
    target.mr_classlist = finder.castView(view, 2131230840, "field 'mr_classlist'");
    view = finder.findRequiredView(source, 2131230839, "field 'mr_rollad'");
    target.mr_rollad = finder.castView(view, 2131230839, "field 'mr_rollad'");
    view = finder.findRequiredView(source, 2131230834, "field 'mr_slidebtn' and method 'mr_slidebtn'");
    target.mr_slidebtn = finder.castView(view, 2131230834, "field 'mr_slidebtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.mr_slidebtn();
        }
      });
    view = finder.findRequiredView(source, 2131230838, "field 'mr_refreshview'");
    target.mr_refreshview = finder.castView(view, 2131230838, "field 'mr_refreshview'");
  }

  @Override public void reset(T target) {
    target.ml_menu = null;
    target.main_reside = null;
    target.mr_classlist = null;
    target.mr_rollad = null;
    target.mr_slidebtn = null;
    target.mr_refreshview = null;
  }
}
