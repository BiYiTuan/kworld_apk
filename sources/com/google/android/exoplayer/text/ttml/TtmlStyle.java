package com.google.android.exoplayer.text.ttml;

import android.text.Layout.Alignment;
import com.google.android.exoplayer.util.Assertions;

final class TtmlStyle {
    public static final short FONT_SIZE_UNIT_EM = (short) 2;
    public static final short FONT_SIZE_UNIT_PERCENT = (short) 3;
    public static final short FONT_SIZE_UNIT_PIXEL = (short) 1;
    private static final short OFF = (short) 0;
    private static final short ON = (short) 1;
    public static final short STYLE_BOLD = (short) 1;
    public static final short STYLE_BOLD_ITALIC = (short) 3;
    public static final short STYLE_ITALIC = (short) 2;
    public static final short STYLE_NORMAL = (short) 0;
    public static final short UNSPECIFIED = (short) -1;
    private int backgroundColor;
    private boolean backgroundColorSpecified;
    private short bold = (short) -1;
    private int color;
    private boolean colorSpecified;
    private String fontFamily;
    private float fontSize;
    private short fontSizeUnit = (short) -1;
    private String id;
    private TtmlStyle inheritableStyle;
    private short italic = (short) -1;
    private short linethrough = (short) -1;
    private Alignment textAlign;
    private short underline = (short) -1;

    TtmlStyle() {
    }

    public short getStyle() {
        if (this.bold == (short) -1 && this.italic == (short) -1) {
            return (short) -1;
        }
        short style = (short) 0;
        if (this.bold != (short) -1) {
            style = (short) (this.bold + 0);
        }
        if (this.italic != (short) -1) {
            return (short) (this.italic + style);
        }
        return style;
    }

    public boolean isLinethrough() {
        return this.linethrough == (short) 1;
    }

    public TtmlStyle setLinethrough(boolean linethrough) {
        boolean z;
        short s = (short) 1;
        if (this.inheritableStyle == null) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        if (!linethrough) {
            s = (short) 0;
        }
        this.linethrough = s;
        return this;
    }

    public boolean isUnderline() {
        return this.underline == (short) 1;
    }

    public TtmlStyle setUnderline(boolean underline) {
        boolean z;
        short s = (short) 1;
        if (this.inheritableStyle == null) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        if (!underline) {
            s = (short) 0;
        }
        this.underline = s;
        return this;
    }

    public String getFontFamily() {
        return this.fontFamily;
    }

    public TtmlStyle setFontFamily(String fontFamily) {
        Assertions.checkState(this.inheritableStyle == null);
        this.fontFamily = fontFamily;
        return this;
    }

    public int getColor() {
        return this.color;
    }

    public TtmlStyle setColor(int color) {
        Assertions.checkState(this.inheritableStyle == null);
        this.color = color;
        this.colorSpecified = true;
        return this;
    }

    public boolean hasColorSpecified() {
        return this.colorSpecified;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public TtmlStyle setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.backgroundColorSpecified = true;
        return this;
    }

    public boolean hasBackgroundColorSpecified() {
        return this.backgroundColorSpecified;
    }

    public TtmlStyle setBold(boolean isBold) {
        boolean z;
        short s = (short) 1;
        if (this.inheritableStyle == null) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        if (!isBold) {
            s = (short) 0;
        }
        this.bold = s;
        return this;
    }

    public TtmlStyle setItalic(boolean isItalic) {
        boolean z;
        short s = (short) 0;
        if (this.inheritableStyle == null) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        if (isItalic) {
            s = (short) 2;
        }
        this.italic = s;
        return this;
    }

    public TtmlStyle inherit(TtmlStyle ancestor) {
        return inherit(ancestor, false);
    }

    public TtmlStyle chain(TtmlStyle ancestor) {
        return inherit(ancestor, true);
    }

    private TtmlStyle inherit(TtmlStyle ancestor, boolean chaining) {
        if (ancestor != null) {
            if (!this.colorSpecified && ancestor.colorSpecified) {
                setColor(ancestor.color);
            }
            if (this.bold == (short) -1) {
                this.bold = ancestor.bold;
            }
            if (this.italic == (short) -1) {
                this.italic = ancestor.italic;
            }
            if (this.fontFamily == null) {
                this.fontFamily = ancestor.fontFamily;
            }
            if (this.linethrough == (short) -1) {
                this.linethrough = ancestor.linethrough;
            }
            if (this.underline == (short) -1) {
                this.underline = ancestor.underline;
            }
            if (this.textAlign == null) {
                this.textAlign = ancestor.textAlign;
            }
            if (this.fontSizeUnit == (short) -1) {
                this.fontSizeUnit = ancestor.fontSizeUnit;
                this.fontSize = ancestor.fontSize;
            }
            if (chaining && !this.backgroundColorSpecified && ancestor.backgroundColorSpecified) {
                setBackgroundColor(ancestor.backgroundColor);
            }
        }
        return this;
    }

    public TtmlStyle setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return this.id;
    }

    public Alignment getTextAlign() {
        return this.textAlign;
    }

    public TtmlStyle setTextAlign(Alignment textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    public TtmlStyle setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public TtmlStyle setFontSizeUnit(short unit) {
        this.fontSizeUnit = unit;
        return this;
    }

    public short getFontSizeUnit() {
        return this.fontSizeUnit;
    }

    public float getFontSize() {
        return this.fontSize;
    }
}
