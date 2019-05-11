package com.android.ledgerbook.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.TextView;

import com.android.ledgerbook.R;
import com.android.ledgerbook.utils.StringUtils;
import com.android.ledgerbook.utils.TypefaceUtils;
import com.android.ledgerbook.utils.Validatable;
import com.android.ledgerbook.utils.Validator;

import java.util.regex.Pattern;

public class CustomTextInput extends TextInputEditText implements Validatable {
    String errorMsg;
    private Validator validator;
    private int validatorType;
    private String prefix;
    protected CustomInputBox parentInputBox;
    private int inputType;
    private String mandatoryErrorString;

    public static final int INPUT_TYPE_CAP_PARAGRAPH = 1;
    public static final int INPUT_TYPE_AMOUNT = 2;

    public CustomTextInput(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(context, null);
        }
    }

    public CustomTextInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public CustomTextInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    protected void init(@NonNull Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextInput);
        inputType = a.getInt(R.styleable.CustomTextInput_inputType, 0);
        if (a.hasValue(R.styleable.CustomTextInput_validatorType)) {
            validatorType = a.getInt(R.styleable.CustomTextInput_validatorType, -1);
        } else {
            validatorType = -1;
        }

        final int errorStrId;
        if (a.hasValue(R.styleable.CustomTextInput_mandatoryText)) {
            errorStrId = a.getResourceId(R.styleable.CustomTextInput_mandatoryText,
                    R.string.this_field_cannot_be_blank);
        } else {
            errorStrId = R.string.this_field_cannot_be_blank;
        }
        mandatoryErrorString = context.getString(errorStrId);
        a.recycle();
        TypefaceUtils.initView(this, context, attrs);
        setValidatorFromType(validatorType);
        addInitialTextChangeListener();
    }

    private void addInitialTextChangeListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkValidatorTypePrefix(s);
                if (parentInputBox == null) {
                    parentInputBox = getParentView();
                }

                if (parentInputBox != null) {
                    parentInputBox.setErrorEnabled(false);
                }

                if (inputType != 0) {
                    switch (inputType) {
                        case INPUT_TYPE_CAP_PARAGRAPH:
                            String strData = s.toString();
                            if (!TextUtils.isEmpty(strData.trim()) && strData.length() > 0) {
                                Character firstChar = strData.charAt(0);
                                if (!firstChar.equals(Character.toUpperCase(firstChar))) {
                                    s.replace(0, 1, String.valueOf(Character.toUpperCase(firstChar)));
                                }
                            }
                            break;
                        case INPUT_TYPE_AMOUNT:
                            CharSequence rupeeValue;
                            if (prefix != null && s.length() > prefix.length()) {
                                rupeeValue = s.subSequence(prefix.length(), s.length());
                                if (rupeeValue != null) {
                                    int rupeeValueLength = rupeeValue.length();
                                    if (rupeeValueLength > 3) {
                                        StringBuilder onlyNumberRupeeValue = new StringBuilder();
                                        for (int i = 0; i < rupeeValueLength; i++) {
                                            char charValue = rupeeValue.charAt(i);
                                            if (Character.isDigit(charValue)) {
                                                onlyNumberRupeeValue.append(charValue);
                                            }
                                        }

                                        int amountValue = Integer.parseInt(onlyNumberRupeeValue.toString());
                                        if (amountValue > (Integer.MAX_VALUE / 100)) {
                                            amountValue /= 10;
                                        }
                                        int currentAmount = amountValue * 100;
                                        if (currentAmount != 0) {
                                            String formattedAmount = getContext()
                                                    .getString(R.string.rupee_symbol_amount,
                                                            StringUtils.formatPaise(currentAmount));
                                            if (!s.toString().equals(formattedAmount)) {
                                                setText(formattedAmount);
                                                Selection.setSelection(getText(), getText().length());
                                            }
                                        }
                                    }
                                }
                            }
                    }
                }
            }
        });
    }

    private void checkValidatorTypePrefix(Editable s) {
        if (validatorType != -1) {
            if (!TextUtils.isEmpty(prefix)) {
                if (!s.toString().startsWith(prefix)) {
                    String refinedString = s.toString().replace(prefix, "");
                    String strWithPrefix = prefix + (refinedString.length() >
                            prefix.length() ? refinedString : "");
                    setText(strWithPrefix);
                    Selection.setSelection(getText(), getText().length());
                } else if (validatorType == Validator.EMAIL) {
                    if (s.length() > 0 && s.toString().charAt(0) == '+') {
                        s.delete(0, 1);
                    }
                }
            }
        }
    }

    public void setValidatorFromType(int validatorType) {
        this.validatorType = validatorType;
        switch (validatorType) {
            case Validator.MANDATORY:
                validator = view -> {
                    String data = ((TextView) view).getText().toString().trim();
                    if (TextUtils.isEmpty(data)) {
                        return mandatoryErrorString;
                    } else {
                        return Validator.NO_ERROR;
                    }
                };
            case Validator.OTP:
                validator = view -> {
                    String otp = ((TextView) view).getText().toString();
                    Context context = view.getContext();
                    if (TextUtils.isEmpty(otp)) {
                        return context.getString(R.string.otp_cannot_be_empty);
                    } else if (otp.length() != 4) {
                        return context.getString(R.string.otp_is_invalid);
                    } else {
                        return Validator.NO_ERROR;
                    }
                };
                break;
            case Validator.PHONE:
                validator = view -> {
                    String phoneNumber = ((CustomTextInput) view).getPrefixLessText();
                    Context context = view.getContext();
                    Pattern phonePattern = Pattern.compile("^[6789]\\d{9}$");
                    if (TextUtils.isEmpty(phoneNumber)) {
                        return context.getString(R.string.add_mobile_number);
                    } else if (phoneNumber.length() != 10) {
                        return context.getString(R.string.mobile_number_of_incorrect_size);
                    } else if (!phonePattern.matcher(phoneNumber).matches()) {
                        return context.getString(R.string.incorrect_number);
                    } else {
                        return Validator.NO_ERROR;
                    }
                };
                break;
            case Validator.EMAIL:
                validator = view -> {
                    String email = ((TextView) view).getText().toString();
                    Context context = view.getContext();
                    if (TextUtils.isEmpty(email)) {
                        return context.getString(R.string.add_email);
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        return context.getString(R.string.invalid_email);
                    } else {
                        return Validator.NO_ERROR;
                    }
                };
                break;
            case Validator.AMOUNT:
                inputType = INPUT_TYPE_AMOUNT;
                validator = view -> {
                    Context context = view.getContext();
                    String strAmount = ((CustomTextInput) view).getPrefixLessText();
                    if (TextUtils.isEmpty(strAmount)) {
                        return context.getString(R.string.enter_amount);
                    } else {
                        String rupeeValue = strAmount.replace(",", "");
                        try {
                            int amountValue = Integer.parseInt(rupeeValue);
                            if (amountValue == 0) {
                                return context.getString(R.string.amount_zero);
                            } else {
                                return Validator.NO_ERROR;
                            }
                        } catch (NumberFormatException e) {
                            return context.getString(R.string.amount_invalid);
                        }
                    }
                };
        }
        addValidatorTypePrefix();
    }

    public String getPrefixLessText() {
        Editable s = getText();
        return removeValidatorPrefix(s);
    }

    private String removeValidatorPrefix(Editable s) {
        if (validatorType != -1) {
            if (!TextUtils.isEmpty(prefix)) {
                return s.subSequence(prefix.length(), s.length()).toString();
            }
        }
        return s.toString().trim();
    }

    private void addValidatorTypePrefix() {
        if (validatorType != -1) {
            if (validatorType == Validator.AMOUNT) {
                prefix = getContext().getString(R.string.rupee_symbol);
            }
            if (prefix != null) {
                setText(prefix);
                Selection.setSelection(getText(), getText().length());
            }
        }
    }

    @Override
    public boolean isValid() {
        if (validator != null) {
            errorMsg = validator.runValidation(this);
            return Validator.NO_ERROR.equals(errorMsg);
        }

        return true;
    }

    @Override
    public void handleError(boolean isError) {
        if (parentInputBox == null) {
            parentInputBox = getParentView();
        }
        if (isError) {
            setErrorInParentView(errorMsg);
        } else {
            setParentErrorEnabled(false);
        }
    }

    protected CustomInputBox getParentView() {
        ViewParent parentView = getParent();
        if (parentView != null) {
            ViewParent inputBoxView = parentView.getParent().getParent().getParent();
            if (inputBoxView instanceof CustomInputBox) {
                return (CustomInputBox) inputBoxView;
            }
        }
        return null;
    }

    protected void setParentErrorEnabled(boolean enabled) {
        if (parentInputBox != null) {
            parentInputBox.setErrorEnabled(enabled);
        }
    }

    protected void setErrorInParentView(CharSequence error) {
        if (parentInputBox != null) {
            if (!Validator.NO_ERROR.equals(error)) {
                parentInputBox.setError(error);
            }
        }
    }
}
