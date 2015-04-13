// Generated code from Butter Knife. Do not modify!
package com.septinary.xbwapp.activity.details;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class TeacherDetailsActivity$$ViewInjector<T extends com.septinary.xbwapp.activity.details.TeacherDetailsActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230820, "field 'ltd_viewpager'");
    target.ltd_viewpager = finder.castView(view, 2131230820, "field 'ltd_viewpager'");
  }

  @Override public void reset(T target) {
    target.ltd_viewpager = null;
  }
}
