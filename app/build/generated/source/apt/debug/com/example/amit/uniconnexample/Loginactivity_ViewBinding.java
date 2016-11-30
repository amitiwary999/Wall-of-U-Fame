// Generated code from Butter Knife. Do not modify!
package com.example.amit.uniconnexample;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class Loginactivity_ViewBinding<T extends Loginactivity> implements Unbinder {
  protected T target;

  private View view2131558541;

  private View view2131558542;

  public Loginactivity_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    target.email = finder.findRequiredViewAsType(source, R.id.email, "field 'email'", EditText.class);
    target.password = finder.findRequiredViewAsType(source, R.id.password, "field 'password'", EditText.class);
    view = finder.findRequiredView(source, R.id.log_in, "field 'login' and method 'login'");
    target.login = finder.castView(view, R.id.log_in, "field 'login'", Button.class);
    view2131558541 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.login();
      }
    });
    view = finder.findRequiredView(source, R.id.signup, "field 'sign_up' and method 'signup'");
    target.sign_up = finder.castView(view, R.id.signup, "field 'sign_up'", Button.class);
    view2131558542 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.signup();
      }
    });
    target.loginProgress = finder.findRequiredViewAsType(source, R.id.login_progress, "field 'loginProgress'", ProgressBar.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.email = null;
    target.password = null;
    target.login = null;
    target.sign_up = null;
    target.loginProgress = null;

    view2131558541.setOnClickListener(null);
    view2131558541 = null;
    view2131558542.setOnClickListener(null);
    view2131558542 = null;

    this.target = null;
  }
}
