// Generated code from Butter Knife. Do not modify!
package com.example.amit.uniconnexample;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class Profile_ViewBinding<T extends Profile> implements Unbinder {
  protected T target;

  private View view2131558546;

  private View view2131558550;

  public Profile_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    target.toolbar = finder.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.name = finder.findRequiredViewAsType(source, R.id.name, "field 'name'", EditText.class);
    target.email = finder.findRequiredViewAsType(source, R.id.email, "field 'email'", EditText.class);
    target.phone = finder.findRequiredViewAsType(source, R.id.phone, "field 'phone'", EditText.class);
    view = finder.findRequiredView(source, R.id.photo, "field 'photo' and method 'pickPhoto'");
    target.photo = finder.castView(view, R.id.photo, "field 'photo'", ImageView.class);
    view2131558546 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pickPhoto();
      }
    });
    target.loading = finder.findRequiredViewAsType(source, R.id.loading, "field 'loading'", LinearLayout.class);
    view = finder.findRequiredView(source, R.id.save, "method 'save'");
    view2131558550 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.save();
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.name = null;
    target.email = null;
    target.phone = null;
    target.photo = null;
    target.loading = null;

    view2131558546.setOnClickListener(null);
    view2131558546 = null;
    view2131558550.setOnClickListener(null);
    view2131558550 = null;

    this.target = null;
  }
}
