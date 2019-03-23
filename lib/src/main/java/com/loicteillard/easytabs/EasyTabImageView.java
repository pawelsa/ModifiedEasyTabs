package com.loicteillard.easytabs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import loic.teillard.easytabs.R;

public class EasyTabImageView extends androidx.appcompat.widget.AppCompatImageView implements EasyTabView {
    
    private int mSelectedColor, mUnselectedColor;
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    public EasyTabImageView(Context context) {
        super(context);
        initialize(null);
    }
    
    public EasyTabImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }
    
    public EasyTabImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    private void initialize(AttributeSet attrs) {
        
        // Read custom values
        TypedArray attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.EasyTabsAttrs, 0, 0);
        initAttributesArray(attrsArray);
        attrsArray.recycle();
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    private void initAttributesArray(TypedArray attrsArray) {
        
        if (attrsArray == null) {
            return;
        }
        
        mSelectedColor = attrsArray.getColor(R.styleable.EasyTabsAttrs_selected_color, 0);
        mUnselectedColor = attrsArray.getColor(R.styleable.EasyTabsAttrs_unselected_color, 0);
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    @Override
    public int getSelectedColor() {
        return mSelectedColor;
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    @Override
    public int getUnselectedColor() {
        return mUnselectedColor;
    }
    
    // ---------------------------------------------------------------------------------------------------------------------
}
