// Generated code from Butter Knife. Do not modify!
package com.septinary.xbwapp.activity.list;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class TeacherListActivity$$ViewInjector<T extends com.septinary.xbwapp.activity.list.TeacherListActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230827, "field 'lt_teacherlist'");
    target.lt_teacherlist = finder.castView(view, 2131230827, "field 'lt_teacherlist'");
    view = finder.findRequiredView(source, 2131230818, "field 'lt_back' and method 'lt_back'");
    target.lt_back = finder.castView(view, 2131230818, "field 'lt_back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.lt_back();
        }
      });
  }

  @Override public void reset(T target) {
    target.lt_teacherlist = null;
    target.lt_back = null;
  }
}
