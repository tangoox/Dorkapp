// Generated code from Butter Knife. Do not modify!
package com.septinary.xbwapp.activity.main;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class MainActivity$$ViewInjector<T extends com.septinary.xbwapp.activity.main.MainActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230878, "field 'mr_content'");
    target.mr_content = finder.castView(view, 2131230878, "field 'mr_content'");
    view = finder.findRequiredView(source, 2131230835, "field 'main_reside'");
    target.main_reside = finder.castView(view, 2131230835, "field 'main_reside'");
    view = finder.findRequiredView(source, 2131230874, "field 'mr_slidebtn' and method 'mr_slidebtn'");
    target.mr_slidebtn = finder.castView(view, 2131230874, "field 'mr_slidebtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.mr_slidebtn();
        }
      });
  }

  @Override public void reset(T target) {
    target.mr_content = null;
    target.main_reside = null;
    target.mr_slidebtn = null;
  }
}
