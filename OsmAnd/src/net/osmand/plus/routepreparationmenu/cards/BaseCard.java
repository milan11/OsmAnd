package net.osmand.plus.routepreparationmenu.cards;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import net.osmand.plus.utils.ColorUtilities;
import net.osmand.plus.OsmandApplication;
import net.osmand.plus.R;
import net.osmand.plus.helpers.AndroidUiHelper;

public abstract class BaseCard {

	protected final OsmandApplication app;
	protected final FragmentActivity activity;

	protected View view;

	boolean showTopShadow;
	boolean showBottomShadow;
	boolean showDivider = true;
	boolean transparentBackground;
	protected boolean nightMode;

	private CardListener listener;

	public interface CardListener {
		default void onCardLayoutNeeded(@NonNull BaseCard card) {
		}

		default void onCardPressed(@NonNull BaseCard card) {
		}

		default void onCardButtonPressed(@NonNull BaseCard card, int buttonIndex) {
		}
	}

	public BaseCard(@NonNull FragmentActivity activity) {
		this(activity, true);
	}

	public BaseCard(@NonNull FragmentActivity activity, boolean usedOnMap) {
		this.activity = activity;
		this.app = (OsmandApplication) activity.getApplicationContext();
		nightMode = usedOnMap ? app.getDaynightHelper().isNightModeForMapControls() : !app.getSettings().isLightContent();
	}

	public abstract int getCardLayoutId();

	@Nullable
	public View getView() {
		return view;
	}

	public int getViewHeight() {
		return view != null ? view.getHeight() : 0;
	}

	public int getTopViewHeight() {
		return getViewHeight();
	}

	public void update() {
		if (view != null) {
			updateContent();
		}
	}

	public void applyState(@NonNull BaseCard card) {
		// non implemented
	}

	public CardListener getListener() {
		return listener;
	}

	public void setListener(CardListener listener) {
		this.listener = listener;
	}

	protected void notifyCardPressed() {
		if (listener != null) {
			listener.onCardPressed(this);
		}
	}

	protected void notifyButtonPressed(int buttonIndex) {
		if (listener != null) {
			listener.onCardButtonPressed(this, buttonIndex);
		}
	}

	public void setLayoutNeeded() {
		CardListener listener = this.listener;
		if (listener != null) {
			listener.onCardLayoutNeeded(this);
		}
	}

	protected abstract void updateContent();

	public View build(Context ctx) {
		ContextThemeWrapper context =
				new ContextThemeWrapper(ctx, !nightMode ? R.style.OsmandLightTheme : R.style.OsmandDarkTheme);
		view = LayoutInflater.from(context).inflate(getCardLayoutId(), null);
		update();
		return view;
	}

	public OsmandApplication getMyApplication() {
		return app;
	}

	@ColorInt
	protected int getResolvedColor(@ColorRes int colorId) {
		return ContextCompat.getColor(app, colorId);
	}

	@ColorInt
	protected int getActiveColor() {
		return ColorUtilities.getActiveColor(app, nightMode);
	}

	@ColorInt
	protected int getMainFontColor() {
		return ColorUtilities.getPrimaryTextColor(app, nightMode);
	}

	@ColorInt
	protected int getSecondaryColor() {
		return getResolvedColor(R.color.description_font_and_bottom_sheet_icons);
	}

	protected Drawable getContentIcon(@DrawableRes int icon) {
		return getColoredIcon(icon, R.color.description_font_and_bottom_sheet_icons);
	}

	protected Drawable getActiveIcon(@DrawableRes int icon) {
		return getColoredIcon(icon, ColorUtilities.getActiveColorId(nightMode));
	}

	protected Drawable getIcon(@DrawableRes int icon) {
		return app.getUIUtilities().getIcon(icon);
	}

	protected Drawable getColoredIcon(@DrawableRes int icon, @ColorRes int color) {
		return app.getUIUtilities().getIcon(icon, color);
	}

	protected Drawable getPaintedIcon(@DrawableRes int id, @ColorInt int color) {
		return app.getUIUtilities().getPaintedIcon(id, color);
	}

	protected int getDimen(@DimenRes int dimenId) {
		return app.getResources().getDimensionPixelSize(dimenId);
	}

	public void setShowTopShadow(boolean showTopShadow) {
		this.showTopShadow = showTopShadow;
	}

	public void setShowBottomShadow(boolean showBottomShadow) {
		this.showBottomShadow = showBottomShadow;
	}

	public boolean isShowDivider() {
		return showDivider;
	}

	public void setShowDivider(boolean showDivider) {
		this.showDivider = showDivider;
	}

	public boolean isTransparentBackground() {
		return transparentBackground;
	}

	public void setTransparentBackground(boolean transparentBackground) {
		this.transparentBackground = transparentBackground;
	}

	public void updateVisibility(boolean show) {
		AndroidUiHelper.updateVisibility(view, show);
	}

	public boolean isVisible() {
		return view != null && view.getVisibility() == View.VISIBLE;
	}
}