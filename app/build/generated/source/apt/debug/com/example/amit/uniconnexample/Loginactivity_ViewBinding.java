// Generated code from Butter Knife. Do not modify!
package com.example.amit.uniconnexample;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class Loginactivity_ViewBinding<T extends Loginactivity> implements Unbinder {
  protected T target;

  private View view2131755163;

  private View view2131755162;

  public Loginactivity_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    view = finder.findRequiredView(source, R.id.signup, "method 'signup'");
    view2131755163 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.signup();
      }
    });
    view = finder.findRequiredView(source, R.id.log_in, "method 'login'");
    view2131755162 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.login();
      }
    });
  }

  @Override
  public void unbind() {
    if (this.target == null) throw new IllegalStateException("Bindings already cleared.");

    view2131755163.setOnClickListener(null);
    view2131755163 = null;
    view2131755162.setOnClickListener(null);
    view2131755162 = null;

    this.target = null;
  }
}
