package com.arzen.iFoxLib.dynamic.base;

import com.arzen.iFoxLib.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ProgressDynamic extends BaseDynamic {

	private View mProgressContainer;
	private View mContentContainer;
	private View mContentError;
	private View mContentView;
	private View mEmptyView;
	private TextView mTvError;
	private boolean mContentShown;
	private boolean mIsContentEmpty;
	
	private View mParentView;
	@Override
	public void onCreate(Activity activity, Bundle bundle) {
		super.onCreate(activity, bundle);
	}
	
	@Override
	public View onCreateView(Activity activity) {
		// TODO Auto-generated method stub
		mParentView = LayoutInflater.from(activity).inflate(R.layout.fragment_progress, null);
		return mParentView;
	}

	@Override
	public void onActivityCreate() {
		ensureContent();
	}

	
	public View getView(){
		return mParentView;
	}
	
	
	/**
	 * Detach from view.
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mContentShown = false;
		mIsContentEmpty = false;
		mProgressContainer = mContentContainer = mContentView = mEmptyView = null;
	}

	/**
	 * Return content view or null if the content view has not been initialized.
	 * 
	 * @return content view or null
	 * @see #setContentView(android.view.View)
	 * @see #setContentView(int)
	 */
	public View getContentView() {
		return mContentView;
	}

	/**
	 * Set the content content from a layout resource.
	 * 
	 * @param layoutResId
	 *            Resource ID to be inflated.
	 * @see #setContentView(android.view.View)
	 * @see #getContentView()
	 */
	public void setContentView(int layoutResId) {
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		View contentView = layoutInflater.inflate(layoutResId, null);
		setContentView(contentView);
	}

	/**
	 * Set the content view to an explicit view. If the content view was
	 * installed earlier, the content will be replaced with a new view.
	 * 
	 * @param view
	 *            The desired content to display. Value can't be null.
	 * @see #setContentView(int)
	 * @see #getContentView()
	 */
	public void setContentView(View view) {
		ensureContent();
		if (view == null) {
			throw new IllegalArgumentException("Content view can't be null");
		}
		if (mContentContainer instanceof ViewGroup) {
			ViewGroup contentContainer = (ViewGroup) mContentContainer;
			if (mContentView == null) {
				contentContainer.addView(view);
			} else {
				int index = contentContainer.indexOfChild(mContentView);
				// replace content view
				contentContainer.removeView(mContentView);
				contentContainer.addView(view, index);
			}
			mContentView = view;
		} else {
			throw new IllegalStateException("Can't be used with a custom content view");
		}
	}

	/**
	 * The default content for a ProgressFragment has a TextView that can be
	 * shown when the content is empty {@link #setContentEmpty(boolean)}. If you
	 * would like to have it shown, call this method to supply the text it
	 * should use.
	 * 
	 * @param resId
	 *            Identification of string from a resources
	 * @see #setEmptyText(CharSequence)
	 */
	public void setEmptyText(int resId) {
		setEmptyText(getString(resId));
	}

	/**
	 * The default content for a ProgressFragment has a TextView that can be
	 * shown when the content is empty {@link #setContentEmpty(boolean)}. If you
	 * would like to have it shown, call this method to supply the text it
	 * should use.
	 * 
	 * @param text
	 *            Text for empty view
	 * @see #setEmptyText(int)
	 */
	public void setEmptyText(final CharSequence text) {
		getMainHandler().post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ensureContent();
				if (mEmptyView != null && mEmptyView instanceof TextView) {
					((TextView) mEmptyView).setText(text);
				} else {
					throw new IllegalStateException("Can't be used with a custom content view");
				}
			}
		});
	}

	/**
	 * Control whether the content is being displayed. You can make it not
	 * displayed if you are waiting for the initial data to show in it. During
	 * this time an indeterminant progress indicator will be shown instead.
	 * 
	 * @param shown
	 *            If true, the content view is shown; if false, the progress
	 *            indicator. The initial value is true.
	 * @see #setContentShownNoAnimation(boolean)
	 */
	public void setContentShown(boolean shown) {
		setContentShown(shown, true);
	}

	/**
	 * Like {@link #setContentShown(boolean)}, but no animation is used when
	 * transitioning from the previous state.
	 * 
	 * @param shown
	 *            If true, the content view is shown; if false, the progress
	 *            indicator. The initial value is true.
	 * @see #setContentShown(boolean)
	 */
	public void setContentShownNoAnimation(boolean shown) {
		setContentShown(shown, false);
	}

	/**
	 * Control whether the content is being displayed. You can make it not
	 * displayed if you are waiting for the initial data to show in it. During
	 * this time an indeterminant progress indicator will be shown instead.
	 * 
	 * @param shown
	 *            If true, the content view is shown; if false, the progress
	 *            indicator. The initial value is true.
	 * @param animate
	 *            If true, an animation will be used to transition to the new
	 *            state.
	 */
	private void setContentShown(boolean shown, boolean animate) {
		ensureContent();
		if (mContentShown == shown) {
			return;
		}
		mContentShown = shown;
		if (shown) {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
				mContentContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
			} else {
				mProgressContainer.clearAnimation();
				mContentContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.GONE);
			mContentContainer.setVisibility(View.VISIBLE);
		} else {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
				mContentContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
			} else {
				mProgressContainer.clearAnimation();
				mContentContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.VISIBLE);
			mContentContainer.setVisibility(View.GONE);
		}
	}

	/**
	 * Returns true if content is empty. The default content is not empty.
	 * 
	 * @return true if content is null or empty
	 * @see #setContentEmpty(boolean)
	 */
	public boolean isContentEmpty() {
		return mIsContentEmpty;
	}

	/**
	 * If the content is empty, then set true otherwise false. The default
	 * content is not empty. You can't call this method if the content view has
	 * not been initialized before {@link #setContentView(android.view.View)}
	 * and content view not null.
	 * 
	 * @param isEmpty
	 *            true if content is empty else false
	 * @see #isContentEmpty()
	 */
	public void setContentEmpty(final boolean isEmpty) {
		getMainHandler().post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ensureContent();
				if (mContentView == null) {
					throw new IllegalStateException("Content view must be initialized before");
				}
				if (isEmpty) {
					mEmptyView.setVisibility(View.VISIBLE);
					mContentView.setVisibility(View.GONE);
				} else {
					mEmptyView.setVisibility(View.GONE);
					mContentView.setVisibility(View.VISIBLE);
				}
				mIsContentEmpty = isEmpty;
			}
		});
	}

	/**
	 * 设置显示错误页
	 * 
	 * @param isError
	 */
	public void setContentError(final boolean isError,final String msg) {
		getMainHandler().post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ensureContent();
				if (mContentView == null) {
					throw new IllegalStateException("Content view must be initialized before");
				}
				if (isError) {
					setContentShown(true);
					mContentError.setVisibility(View.VISIBLE);
					mContentView.setVisibility(View.GONE);
					if(msg != null && !msg.equals("")){
						mTvError.setText(msg);
					}
				} else {
					mContentError.setVisibility(View.GONE);
					mContentView.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	/**
	 * 设置请求错误按钮点击事件
	 * @param onClickListener
	 */
	public void setErrorButtonClick(OnClickListener onClickListener)
	{
		if(mContentError != null){
			mContentError.setOnClickListener(onClickListener);
		}
	}

	/**
	 * Initialization views.
	 */
	private void ensureContent() {
		if (mContentContainer != null && mProgressContainer != null) {
			return;
		}
		View root = getView();
		if (root == null) {
			throw new IllegalStateException("Content view not yet created");
		}
		mProgressContainer = root.findViewById(R.id.progress_container);
		if (mProgressContainer == null) {
			throw new RuntimeException("Your content must have a ViewGroup whose id attribute is 'R.id.progress_container'");
		}
		mContentContainer = root.findViewById(R.id.content_container);
		if (mContentContainer == null) {
			throw new RuntimeException("Your content must have a ViewGroup whose id attribute is 'R.id.content_container'");
		}
		mEmptyView = root.findViewById(android.R.id.empty);
		if (mEmptyView != null) {
			mEmptyView.setVisibility(View.GONE);
		}

		mContentError = root.findViewById(R.id.content_error);
		if (mContentError != null) {
			mContentError.setVisibility(View.GONE);
			mTvError = (TextView) mContentError.findViewById(R.id.tvError);
		}

		mContentShown = true;
		// We are starting without a content, so assume we won't
		// have our data right away and start with the progress indicator.
		if (mContentView == null) {
			setContentShown(false, false);
		}
	}

	@Override
	public boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

}
