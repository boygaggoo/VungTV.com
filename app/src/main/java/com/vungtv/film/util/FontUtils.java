package com.vungtv.film.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.vungtv.film.R;

import java.util.HashMap;

public class FontUtils {
    public static final int NORMAL = 0;
    public static final int ITALIC = 1;
    public static final int BOLD = 2;
    public static final int LIGHT = 3;
    public static final int BOLD_ITALIC = 4;
    public static final int LIGHT_ITALIC = 5;

    public static final int FONT_ROBOTO = 0;
    public static final int FONT_QICKSAND_BOLD = 1;

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    /**
     * Select font and style;
     * @param context
     * @param attrs
     * @return
     */
    public static Typeface selectTypeface(Context context, AttributeSet attrs) {
        @SuppressLint("Recycle")
        TypedArray attributeArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.VtvFont);

        int font = attributeArray.getInt(R.styleable.VtvFont_font, FONT_ROBOTO);
        int style = attributeArray.getInt(R.styleable.VtvFont_textStyle, NORMAL);

        // font quicksand;
        if (font == FONT_QICKSAND_BOLD) {
            return getTypeface("Quicksand-Bold.ttf", context);
        }

        // font roboto
        switch (style) {
            case ITALIC: // italic
                return getTypeface("RobotoCondensed-Italic.ttf", context);
            case BOLD: // bold
                return getTypeface("RobotoCondensed-Bold.ttf", context);
            case LIGHT: //light
                return getTypeface("RobotoCondensed-Light.ttf", context);
            case BOLD_ITALIC: // bold italic
                return getTypeface("RobotoCondensed-BoldItalic.ttf", context);
            case LIGHT_ITALIC: //light italic
                return getTypeface("RobotoCondensed-LightItalic.ttf", context);
            default: //normal
                return getTypeface("RobotoCondensed-Regular.ttf", context);
        }
    }

    /**
     * Get Type face
     * @param context c
     * @param font font
     * @param style textStyle
     * @return Typeface
     */
    public static Typeface selectTypeface(Context context, int font, int style) {

        // font quicksand;
        if (font == FONT_QICKSAND_BOLD) {
            return getTypeface("Quicksand-Bold.ttf", context);
        }

        // font roboto
        switch (style) {
            case ITALIC: // italic
                return getTypeface("RobotoCondensed-Italic.ttf", context);
            case BOLD: // bold
                return getTypeface("RobotoCondensed-Bold.ttf", context);
            case LIGHT: //light
                return getTypeface("RobotoCondensed-Light.ttf", context);
            case BOLD_ITALIC: // bold italic
                return getTypeface("RobotoCondensed-BoldItalic.ttf", context);
            case LIGHT_ITALIC: //light italic
                return getTypeface("RobotoCondensed-LightItalic.ttf", context);
            default: //normal
                return getTypeface("RobotoCondensed-Regular.ttf", context);
        }
    }

    /**
     * Select font roboto with style;
     *
     * @param context
     * @param style
     * @return
     */
    public static Typeface selectTypeface(Context context, int style) {

        // font roboto
        switch (style) {
            case ITALIC: // italic
                return getTypeface("RobotoCondensed-Italic.ttf", context);
            case BOLD: // bold
                return getTypeface("RobotoCondensed-Bold.ttf", context);
            case LIGHT: //light
                return getTypeface("RobotoCondensed-Light.ttf", context);
            case BOLD_ITALIC: // bold italic
                return getTypeface("RobotoCondensed-BoldItalic.ttf", context);
            case LIGHT_ITALIC: //light italic
                return getTypeface("RobotoCondensed-LightItalic.ttf", context);
            default: //normal
                return getTypeface("RobotoCondensed-Regular.ttf", context);
        }
    }

    /**
     * Get font from accets file;
     *
     * @param fontname file name
     * @param context context;
     * @return Typeface
     */
    private static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontname);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }
}
