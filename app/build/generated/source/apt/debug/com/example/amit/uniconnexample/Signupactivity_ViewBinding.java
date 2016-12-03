// Generated code from Butter Knife. Do not modify!
package com.example.amit.uniconnexample;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class Signupactivity_ViewBinding<T extends Signupactivity> implements Unbinder {
  protected T target;

  private View view2131558556;

  private View view2131558558;

  public Signupactivity_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    target.name = finder.findRequiredViewAsType(source, R.id.name, "field 'name'", TextInputLayout.class);
    target.email = finder.findRequiredViewAsType(source, R.id.email, "field 'email'", TextInputLayout.class);
    target.password = finder.findRequiredViewAsType(source, R.id.password, "field 'password'", TextInputLayout.class);
    target.phone = finder.findRequiredViewAsType(source, R.id.phone, "field 'phone'", TextInputLayout.class);
    target.confrmpassword = finder.findRequiredViewAsType(source, R.id.confirmpassword, "field 'confrmpassword'", TextInputLayout.class);
    view = finder.findRequiredView(source, R.id.iview, "field 'iv' and method 'pickPhoto'");
    target.iv = finder.castView(view, R.id.iview, "field 'iv'", ImageView.class);
    view2131558556 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pickPhoto();
      }
    });
    target.clg = finder.findRequiredViewAsType(source, R.id.clg, "field 'clg'", TextInputLayout.class);
    view = finder.findRequiredView(source, R.id.sign_up, "field 'signup' and method 'sup'");
    target.signup = finder.castView(view, R.id.sign_up, "field 'signup'", Button.class);
    view2131558558 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.sup();
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.name = null;
    target.email = null;
    target.password = null;
    target.phone = null;
    target.confrmpassword = null;
    target.iv = null;
    target.clg = null;
    target.signup = null;

    view2131558556.setOnClickListener(null);
    view2131558556 = null;
    view2131558558.setOnClickListener(null);
    view2131558558 = null;

    this.target = null;
  }
}
