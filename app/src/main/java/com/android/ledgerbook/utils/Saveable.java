package com.android.ledgerbook.utils;

import android.os.Bundle;

public interface Saveable {
    public void saveInstanceState(Bundle bundle);
    public void restoreInstanceState(Bundle bundle);
}
