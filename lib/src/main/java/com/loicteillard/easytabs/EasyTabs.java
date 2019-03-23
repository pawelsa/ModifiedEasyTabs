package com.loicteillard.easytabs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.core.view.ViewCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import loic.teillard.easytabs.R;

public class EasyTabs extends LinearLayout {
    
    public static final int SEP_DEFAULT_COLOR = Color.parseColor("#b7b7b7");
    
    public static final int INDICATOR_TEXT = 0;
    public static final int INDICATOR_IMAGE = 0;
    public static final int INDICATOR_VALUE = 1;
    public static final int INDICATOR_MATCH_PARENT = 2;
    
    private View mIndicator;
    private boolean mSeparatorsEnabled;
    private boolean mIndicatorsEnabled;
    private boolean mBoldForSelected;
    private ArrayList<View> mTabs;
    private int mSelectedColor, mUnselectedColor, mSeparatorColor;
    private int mIndicatorWidth, mSeparatorSize;
    private ViewPager mViewPager;
    protected ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int mDefaultTab;
    private PagerListener mPagerListener;
    private int mIndicatorResource;
    private int mSelectedSize;
    private int mSelectorPaddingBottom;
    private int mSelectorPaddingTop;
    private int mIndicatorHeight;
    private int mIndicatorSpeed;
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    public interface PagerListener {
        void onTabSelected(int position);
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    public EasyTabs(Context context) {
        super(context);
        initialize(null);
    }
    
    public EasyTabs(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }
    
    public EasyTabs(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    private void initialize(AttributeSet attrs) {
        
        // Read custom values
        TypedArray attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.EasyTabsAttrs, 0, 0);
        initAttributesArray(attrsArray);
        attrsArray.recycle();
        
        // Prepare current viewgroup layout
        setOrientation(VERTICAL);
        LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(lParams);
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    private void populate() {
        // Prepare layout for tabs
        LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final LinearLayout layoutTabs = new LinearLayout(getContext());
        layoutTabs.setOrientation(HORIZONTAL);
        layoutTabs.setLayoutParams(lParams);
        
        // Add childs views
        for (int i = 0; i < getChildCount(); i++) {
    
            View view = getChildAt(i);
    
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                addTab(prepareTab(textView), i);
            }
    
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                addTab(prepareTab(imageView), i);
            }
    
        }
        
        // Clear views (childs can have only one parent)
        removeAllViews();
        
        // Create custom stuff
        mIndicator = createIndicator();
        
        // Add tabs items
        for (View view : getTabs()) {
            layoutTabs.addView(view);
            if (mSeparatorsEnabled) {
                layoutTabs.addView(createSeparator());
            }
        }
        
        // Add views
        addView(layoutTabs);
        
        // At the end, add views to the main viewgroup
        if (mIndicatorsEnabled) {
            addView(mIndicator);
        }
        
        // Listener to change state
        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    
            }
    
            @Override
            public void onPageSelected(int position) {
                switchState(position);
            }
    
            @Override
            public void onPageScrollStateChanged(int state) {
        
            }
        };
        
        getViewPager().clearOnPageChangeListeners();
        getViewPager().addOnPageChangeListener(mOnPageChangeListener);
        
        // Initial state on the default item
        switchState(mDefaultTab);
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    public ViewPager getViewPager() {
        if (mViewPager == null) {
            throw new IllegalStateException("No ViewPager found, please add a viewpager as a child of the layout !");
        }
        return mViewPager;
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    public PagerAdapter getPagerAdapter() {
        if (mViewPager == null) {
            throw new IllegalStateException("No ViewPager found, please set one !");
        }
        if (mViewPager.getAdapter() == null) {
            throw new IllegalStateException("No Adapter found for this viewpager, please set one !");
        }
        if (mViewPager.getAdapter()
                .getCount() != getTabs().size()) {
            throw new IllegalStateException("Adapter must have the same number of items than tabs !");
        }
        return mViewPager.getAdapter();
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    public void setViewPager(ViewPager viewPager) {
        setViewPager(viewPager, 0);
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    public void setViewPager(ViewPager viewPager, int defaultTab) {
        mViewPager = viewPager;
        mDefaultTab = defaultTab;
        populate();
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    public void setPagerListener(PagerListener pagerListener) {
        mPagerListener = pagerListener;
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    private void initAttributesArray(TypedArray attrsArray) {
        
        if (attrsArray == null) {
            return;
        }
        
        mSelectedColor = attrsArray.getColor(R.styleable.EasyTabsAttrs_selected_color, Color.BLACK);
        mUnselectedColor = attrsArray.getColor(R.styleable.EasyTabsAttrs_unselected_color, Color.BLACK);
        mSeparatorSize = attrsArray.getInt(R.styleable.EasyTabsAttrs_indicator_size, 0);
        mIndicatorWidth = attrsArray.getDimensionPixelSize(R.styleable.EasyTabsAttrs_indicator_width, 0);
        mIndicatorHeight = attrsArray.getDimensionPixelSize(R.styleable.EasyTabsAttrs_indicator_height, ETUtils.dpToPx(5));
        mSeparatorsEnabled = attrsArray.getBoolean(R.styleable.EasyTabsAttrs_separators, false);
        mIndicatorsEnabled = attrsArray.getBoolean(R.styleable.EasyTabsAttrs_indicators, true);
        mBoldForSelected = attrsArray.getBoolean(R.styleable.EasyTabsAttrs_bold_for_selected, false);
        mSeparatorColor = attrsArray.getColor(R.styleable.EasyTabsAttrs_separator_color, SEP_DEFAULT_COLOR);
        mIndicatorResource = attrsArray.getResourceId(R.styleable.EasyTabsAttrs_indicator_shape, R.drawable.rect);
        mSelectedSize = attrsArray.getDimensionPixelSize(R.styleable.EasyTabsAttrs_selected_size, LayoutParams.WRAP_CONTENT);
        mSelectorPaddingBottom = attrsArray.getDimensionPixelSize(R.styleable.EasyTabsAttrs_selected_padding_bottom, ETUtils.dpToPx(5));
        mSelectorPaddingTop = attrsArray.getDimensionPixelSize(R.styleable.EasyTabsAttrs_selected_padding_top, ETUtils.dpToPx(5));
        mIndicatorSpeed = attrsArray.getInt(R.styleable.EasyTabsAttrs_indicator_speed, 200);
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    private void switchState(int selected) {
        
        int selectedColor = mSelectedColor;
        int unselectedColor = mUnselectedColor;
        
        for (int i = 0; i < getPagerAdapter().getCount(); i++) {
            View view = getTabs().get(i);
            
            if (!(view instanceof TextView) && !(view instanceof ImageView)) {
                continue;
            }
            
            if (view instanceof EasyTabView) {
                EasyTabView easyTabView = (EasyTabView) view;
                if (easyTabView.getSelectedColor() != 0) {
                    selectedColor = easyTabView.getSelectedColor();
                }
                if (easyTabView.getUnselectedColor() != 0) {
                    unselectedColor = easyTabView.getUnselectedColor();
                }
            }
            
            if (view instanceof TextView) {
                ((TextView) view).setTextColor((i == selected) ? selectedColor : unselectedColor);
                ((TextView) view).setTypeface(null, i == selected && mBoldForSelected ? Typeface.BOLD : Typeface.NORMAL);
            } else {
                ImageViewCompat.setImageTintList((ImageView) view, ColorStateList.valueOf((i == selected) ? selectedColor : unselectedColor));
            }
            
            if (i == selected) {
                ViewCompat.setBackgroundTintList(mIndicator, ColorStateList.valueOf(selectedColor));
                draw(view);
            }
        }
        
        getViewPager().removeOnPageChangeListener(mOnPageChangeListener);
        getViewPager().setCurrentItem(selected, true);
        getViewPager().addOnPageChangeListener(mOnPageChangeListener);
        
        if (mPagerListener != null) {
            mPagerListener.onTabSelected(selected);
        }
        
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    private void draw(final View tab) {
        tab.post(new Runnable() {
            @Override
            public void run() {
                if (mIndicator.getMeasuredWidth() > 0) {
                    mIndicator.animate()
                            .translationX(tab.getX())
                            .setDuration(mIndicatorSpeed);
                } else {
                    mIndicator.setTranslationX(tab.getX());
                }
                int padding = 0;
                int tabWidth = tab.getMeasuredWidth();
                switch (mSeparatorSize) {
                    case INDICATOR_TEXT: // same as INDICATOR_IMAGE
                        padding = (tab instanceof TextView) ? ETUtils.getTextWidth((TextView) tab) : ((ImageView) tab).getDrawable()
                                .getIntrinsicWidth();
                        break;
                    case INDICATOR_VALUE:
                        padding = mIndicatorWidth;
                        break;
                    case INDICATOR_MATCH_PARENT:
                        padding = tabWidth;
                        break;
                }
                ETUtils.setDimensionLayout(mIndicator, padding, -1);
                ETUtils.setMarginsLayout(mIndicator, (tabWidth - padding) >> 1, -1, (tabWidth - padding) >> 1, -1);
            }
        });
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    private TextView prepareTab(TextView tab) {
        tab.setGravity(Gravity.CENTER);
        tab.setPadding(0, mSelectorPaddingTop, 0, mSelectorPaddingBottom);
        
        LayoutParams textViewParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        textViewParams.weight = 1f;
        textViewParams.gravity = Gravity.CENTER;
        tab.setLayoutParams(textViewParams);
        
        return tab;
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    private ImageView prepareTab(ImageView tab) {
        tab.setPadding(0, mSelectorPaddingTop, 0, mSelectorPaddingBottom);
        
        LayoutParams params = new LayoutParams(0, mSelectedSize);
        params.weight = 1f;
        params.gravity = Gravity.CENTER;
        tab.setLayoutParams(params);
        
        return tab;
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    private View createSeparator() {
        
        View view = new View(getContext());
        view.setBackgroundColor(mSeparatorColor);
        
        LayoutParams params = new LayoutParams(ETUtils.dpToPx(1), ETUtils.dpToPx(15));
        params.gravity = Gravity.END;
        view.setLayoutParams(params);
        
        return view;
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    private View createIndicator() {
        View view = new View(getContext());
        LayoutParams indicatorParams = new LayoutParams(mIndicatorWidth, mIndicatorHeight);
        indicatorParams.gravity = Gravity.TOP;
        if (mIndicatorResource != -1) {
            Drawable drawable = getContext().getResources()
                    .getDrawable(mIndicatorResource);
            drawable.setColorFilter(0xFFF, PorterDuff.Mode.MULTIPLY);
            view.setBackgroundResource(mIndicatorResource);
        }
        view.setLayoutParams(indicatorParams);
        
        return view;
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    public void addTab(View view, final int index) {
        if (view == null) {
            return;
        }
        getTabs().add(view);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switchState(index);
            }
        });
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    public ArrayList<View> getTabs() {
        if (mTabs == null) {
            mTabs = new ArrayList<>();
        }
        return mTabs;
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
}
