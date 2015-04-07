// Generated code from Butter Knife. Do not modify!
package com.septinary.xbwapp.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class LeftMenuAdapter$LeftMenuViewHolder$$ViewInjector<T extends com.septinary.xbwapp.adapter.LeftMenuAdapter.LeftMenuViewHolder> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034174, "field 'imlm_tips'");
    target.imlm_tips = finder.castView(view, 2131034174, "field 'imlm_tips'");
    view = finder.findRequiredView(source, 2131034173, "field 'imlm_text'");
    target.imlm_text = finder.castView(view, 2131034173, "field 'imlm_text'");
    view = finder.findRequiredView(source, 2131034172, "field 'imlm_icon'");
    target.imlm_icon = finder.castView(view, 2131034172, "field 'imlm_icon'");
  }

  @Override public void reset(T target) {
    target.imlm_tips = null;
    target.imlm_text = null;
    target.imlm_icon = null;
  }
}
