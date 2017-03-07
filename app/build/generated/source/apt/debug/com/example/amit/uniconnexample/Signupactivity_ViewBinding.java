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

  private View view2131755273;

  private View view2131755281;

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
    view2131755273 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pickPhoto();
      }
    });
    target.clg = finder.findRequiredViewAsType(source, R.id.clg, "field 'clg'", TextInputLayout.class);
    view = finder.findRequiredView(source, R.id.sign_up, "field 'signup' and method 'sup'");
    target.signup = finder.castView(view, R.id.sign_up, "field 'signup'", Button.class);
    view2131755281 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.sup();
      }
    });
    target.person = finder.findRequiredViewAsType(source, R.id.person, "field 'person'", ImageView.class);
    target.msg = finder.findRequiredViewAsType(source, R.id.msg, "field 'msg'", ImageView.class);
    target.lock1 = finder.findRequiredViewAsType(source, R.id.lock1, "field 'lock1'", ImageView.class);
    target.lock2 = finder.findRequiredViewAsType(source, R.id.lock2, "field 'lock2'", ImageView.class);
    target.college = finder.findRequiredViewAsType(source, R.id.college, "field 'college'", ImageView.class);
    target.phonen = finder.findRequiredViewAsType(source, R.id.phn, "field 'phonen'", ImageView.class);
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
    target.person = null;
    target.msg = null;
    target.lock1 = null;
    target.lock2 = null;
    target.college = null;
    target.phonen = null;

    view2131755273.setOnClickListener(null);
    view2131755273 = null;
    view2131755281.setOnClickListener(null);
    view2131755281 = null;

    this.target = null;
  }
}
